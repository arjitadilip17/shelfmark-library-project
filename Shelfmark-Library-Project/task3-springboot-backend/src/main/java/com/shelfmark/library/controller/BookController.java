package com.shelfmark.library.controller;

import com.shelfmark.library.model.Book;
import com.shelfmark.library.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository books;

    public BookController(BookRepository books) {
        this.books = books;
    }

    // GET /api/books                    -> all books
    // GET /api/books?q=clean&category=.. -> filtered search (used by the React search bar)
    @GetMapping
    public List<Book> list(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false, defaultValue = "") String category
    ) {
        if (q.isBlank() && category.isBlank()) {
            return books.findAll();
        }
        return books.search(q, category);
    }

    @GetMapping("/{id}")
    public Book getOne(@PathVariable Long id) {
        return books.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book book) {
        return books.save(book);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {
        int rows = books.update(id, book);
        if (rows == 0) throw new NoSuchElementException("Book " + id + " not found");
        book.setId(id);
        return book;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        books.deleteById(id);
    }
}
