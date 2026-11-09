package com.shelfmark.loan.controller;

import com.shelfmark.loan.dto.BorrowRequest;
import com.shelfmark.loan.model.Loan;
import com.shelfmark.loan.repository.LoanRepository;
import com.shelfmark.loan.service.LoanCirculationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanCirculationService circulationService;
    private final LoanRepository loanRepo;

    public LoanController(LoanCirculationService circulationService, LoanRepository loanRepo) {
        this.circulationService = circulationService;
        this.loanRepo = loanRepo;
    }

    @GetMapping
    public List<Loan> listAllActive() {
        return loanRepo.findAllActive();
    }

    @GetMapping("/member/{memberId}")
    public List<Loan> listByMember(@PathVariable Long memberId) {
        return circulationService.getActiveLoansByMember(memberId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan borrow(@RequestBody BorrowRequest req) {
        return circulationService.borrowBook(req.getBookId(), req.getMemberId());
    }

    @PostMapping("/{loanId}/return")
    public Loan returnBook(@PathVariable Long loanId, @RequestBody Map<String, Long> body) {
        Long bookId = body.get("bookId");
        return circulationService.returnBook(loanId, bookId);
    }
}
