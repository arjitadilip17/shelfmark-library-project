package com.shelfmark.book.repository;

import com.shelfmark.book.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbc;

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Book> mapper = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("category"),
            rs.getString("call_no"),
            rs.getInt("copies")
    );

    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books ORDER BY id", mapper);
    }

    public Optional<Book> findById(Long id) {
        List<Book> list = jdbc.query("SELECT * FROM books WHERE id = ?", mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public List<Book> search(String query, String category) {
        String sql = "SELECT * FROM books WHERE 1=1";
        if (query != null && !query.isBlank()) {
            sql += " AND (LOWER(title) LIKE LOWER(?) OR LOWER(author) LIKE LOWER(?))";
        }
        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("All categories")) {
            sql += " AND category = ?";
        }
        sql += " ORDER BY id";

        if (query != null && !query.isBlank() && category != null && !category.isBlank() && !category.equalsIgnoreCase("All categories")) {
            String q = "%" + query + "%";
            return jdbc.query(sql, mapper, q, q, category);
        } else if (query != null && !query.isBlank()) {
            String q = "%" + query + "%";
            return jdbc.query(sql, mapper, q, q);
        } else if (category != null && !category.isBlank() && !category.equalsIgnoreCase("All categories")) {
            return jdbc.query(sql, mapper, category);
        }
        return findAll();
    }

    public boolean decrementCopy(Long id) {
        int updated = jdbc.update("UPDATE books SET copies = copies - 1 WHERE id = ? AND copies > 0", id);
        return updated > 0;
    }

    public boolean incrementCopy(Long id) {
        int updated = jdbc.update("UPDATE books SET copies = copies + 1 WHERE id = ?", id);
        return updated > 0;
    }

    public Book save(Book book) {
        jdbc.update("INSERT INTO books (title, author, category, call_no, copies) VALUES (?, ?, ?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getCategory(), book.getCallNo(), book.getCopies());
        Long id = jdbc.queryForObject("SELECT MAX(id) FROM books", Long.class);
        book.setId(id);
        return book;
    }
}
