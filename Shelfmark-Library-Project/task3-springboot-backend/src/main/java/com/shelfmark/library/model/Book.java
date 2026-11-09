package com.shelfmark.library.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Plain Java object mapped by hand to the BOOKS table via JdbcTemplate's RowMapper
 * (see repository/BookRepository.java). No JPA/Hibernate magic - this is the raw
 * JDBC approach the task asks for.
 */
public class Book {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Call number is required")
    private String callNo;

    @Min(value = 0, message = "Copies cannot be negative")
    private int copies;

    public Book() {
    }

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
