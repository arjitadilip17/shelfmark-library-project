package com.shelfmark.loan.service;

import com.shelfmark.loan.dto.BookDTO;
import com.shelfmark.loan.dto.MemberDTO;
import com.shelfmark.loan.model.Loan;
import com.shelfmark.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanCirculationService {

    private final LoanRepository loanRepo;
    private final RestTemplate restTemplate;

    @Value("${services.book-service.url}")
    private String bookServiceUrl;

    @Value("${services.member-service.url}")
    private String memberServiceUrl;

    public LoanCirculationService(LoanRepository loanRepo, RestTemplate restTemplate) {
        this.loanRepo = loanRepo;
        this.restTemplate = restTemplate;
    }

    public Loan borrowBook(Long bookId, Long memberId) {
        // 1. Verify Member Exists via Member Microservice
        MemberDTO member;
        try {
            member = restTemplate.getForObject(memberServiceUrl + "/api/members/" + memberId, MemberDTO.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member Service verification failed: " + e.getMessage());
        }
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found with ID: " + memberId);
        }

        // 2. Check Book Availability via Book Microservice
        BookDTO book;
        try {
            book = restTemplate.getForObject(bookServiceUrl + "/api/books/" + bookId, BookDTO.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book Service verification failed: " + e.getMessage());
        }
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with ID: " + bookId);
        }
        if (book.getCopies() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No copies available for book: " + book.getTitle());
        }

        // 3. Prevent duplicate active loan for same member and book
        if (loanRepo.findActiveLoan(bookId, memberId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Member already has an active loan for this book.");
        }

        // 4. Atomically Decrement copy in Book Microservice
        try {
            restTemplate.put(bookServiceUrl + "/api/books/" + bookId + "/decrement", null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update book inventory: " + e.getMessage());
        }

        // 5. Determine Loan Duration (Student=14d, Faculty=30d, Public=10d)
        int loanDays = 14;
        if ("Faculty".equalsIgnoreCase(member.getPlan())) loanDays = 30;
        else if ("Public".equalsIgnoreCase(member.getPlan())) loanDays = 10;

        // 6. Record Loan in Loan Database
        Loan loan = loanRepo.createLoan(bookId, memberId, loanDays);
        loan.setBookTitle(book.getTitle());
        loan.setMemberName(member.getName());
        return loan;
    }

    public Loan returnBook(Long loanId, Long bookId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan record not found"));

        if (loan.getReturnedOn() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book has already been returned.");
        }

        // 1. Calculate Overdue Fine (Rs. 2 / day overdue)
        LocalDate today = LocalDate.now();
        BigDecimal fine = BigDecimal.ZERO;
        if (today.isAfter(loan.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), today);
            fine = BigDecimal.valueOf(overdueDays * 2.00);
        }

        // 2. Mark returned in Loan DB
        loanRepo.markReturned(loanId, fine);

        // 3. Atomically Increment copy in Book Microservice
        try {
            restTemplate.put(bookServiceUrl + "/api/books/" + bookId + "/increment", null);
        } catch (Exception e) {
            System.err.println("Warning: Increment copy failed on Book Service: " + e.getMessage());
        }

        loan.setReturnedOn(today);
        loan.setFineAmount(fine);
        return loan;
    }

    public List<Loan> getActiveLoansByMember(Long memberId) {
        List<Loan> loans = loanRepo.findActiveByMemberId(memberId);
        // Enrich titles
        for (Loan l : loans) {
            try {
                BookDTO b = restTemplate.getForObject(bookServiceUrl + "/api/books/" + l.getBookId(), BookDTO.class);
                if (b != null) l.setBookTitle(b.getTitle());
            } catch (Exception ignored) {}
        }
        return loans;
    }
}
