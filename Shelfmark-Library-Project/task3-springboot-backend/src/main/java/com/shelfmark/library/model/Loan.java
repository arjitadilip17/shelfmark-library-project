package com.shelfmark.library.model;

import java.time.LocalDate;

public class Loan {

    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate borrowedOn;
    private LocalDate dueDate;
    private LocalDate returnedOn;

    // Convenience fields filled in by the repository's JOIN query, for a nicer API response
    private String bookTitle;
    private String memberName;

    public Loan() {
    }

    public Loan(Long id, Long bookId, Long memberId, LocalDate borrowedOn, LocalDate dueDate, LocalDate returnedOn) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedOn = borrowedOn;
        this.dueDate = dueDate;
        this.returnedOn = returnedOn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public LocalDate getBorrowedOn() { return borrowedOn; }
    public void setBorrowedOn(LocalDate borrowedOn) { this.borrowedOn = borrowedOn; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnedOn() { return returnedOn; }
    public void setReturnedOn(LocalDate returnedOn) { this.returnedOn = returnedOn; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
}
