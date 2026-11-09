package com.shelfmark.loan.dto;

public class BorrowRequest {
    private Long bookId;
    private Long memberId;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
}
