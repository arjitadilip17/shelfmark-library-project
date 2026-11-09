package com.shelfmark.book.model;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String callNo;
    private int copies;

    public Book() {}

    public Book(Long id, String title, String author, String category, String callNo, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.callNo = callNo;
        this.copies = copies;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCallNo() { return callNo; }
    public void setCallNo(String callNo) { this.callNo = callNo; }

    public int getCopies() { return copies; }
    public void setCopies(int copies) { this.copies = copies; }
}
