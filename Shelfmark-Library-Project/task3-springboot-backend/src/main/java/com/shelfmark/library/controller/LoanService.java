package com.shelfmark.library.controller;

import com.shelfmark.library.model.Book;
import com.shelfmark.library.model.Loan;
import com.shelfmark.library.repository.BookRepository;
import com.shelfmark.library.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Sits between the controller and the two repositories so that "borrow" touches
 * both the books table and the loans table as a single transaction: if either
 * write fails, both roll back and the copy count never goes out of sync.
 */
@Service
public class LoanService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    private final BookRepository books;
    private final LoanRepository loans;

    public LoanService(BookRepository books, LoanRepository loans) {
        this.books = books;
        this.loans = loans;
    }

    public List<Loan> activeLoans() {
        return loans.findActive();
    }

    public List<Loan> loansForMember(Long memberId) {
        return loans.findByMember(memberId);
    }

    @Transactional
    public Loan borrow(Long bookId, Long memberId) {
        Book book = books.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book " + bookId + " not found"));

        if (book.getCopies() <= 0) {
            throw new IllegalStateException("No copies of \"" + book.getTitle() + "\" are available");
        }

        int rowsChanged = books.updateCopies(bookId, -1);
        if (rowsChanged == 0) {
            throw new IllegalStateException("No copies of \"" + book.getTitle() + "\" are available");
        }

        Loan loan = loans.save(bookId, memberId, DEFAULT_LOAN_DAYS);
        loan.setBookTitle(book.getTitle());
        return loan;
    }

    @Transactional
    public void returnBook(Long loanId, Long bookId) {
        int rows = loans.markReturned(loanId);
        if (rows == 0) {
            throw new IllegalStateException("Loan " + loanId + " was already returned or does not exist");
        }
        books.updateCopies(bookId, 1);
    }
}
