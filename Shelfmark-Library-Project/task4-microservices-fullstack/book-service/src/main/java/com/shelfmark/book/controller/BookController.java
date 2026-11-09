package com.shelfmark.book.controller;

import com.shelfmark.book.model.Book;
import com.shelfmark.book.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Book> list(@RequestParam(required = false) String q,
                           @RequestParam(required = false) String category) {
        return repo.search(q, category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return repo.save(book);
    }

    @PutMapping("/{id}/decrement")
    public ResponseEntity<Void> decrementCopy(@PathVariable Long id) {
        boolean ok = repo.decrementCopy(id);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{id}/increment")
    public ResponseEntity<Void> incrementCopy(@PathVariable Long id) {
        boolean ok = repo.incrementCopy(id);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
