package com.shelfmark.library.controller;

import com.shelfmark.library.model.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public List<Loan> active() {
        return loanService.activeLoans();
    }

    @GetMapping("/member/{memberId}")
    public List<Loan> forMember(@PathVariable Long memberId) {
        return loanService.loansForMember(memberId);
    }

    public record BorrowRequest(Long bookId, Long memberId) {}

    // Called when a React user clicks "Borrow this book"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan borrow(@RequestBody BorrowRequest request) {
        return loanService.borrow(request.bookId(), request.memberId());
    }

    public record ReturnRequest(Long bookId) {}

    // Called when a React user clicks "Return"
    @PostMapping("/{loanId}/return")
    public Map<String, String> returnBook(@PathVariable Long loanId, @RequestBody ReturnRequest request) {
        loanService.returnBook(loanId, request.bookId());
        return Map.of("message", "Book returned");
    }
}
