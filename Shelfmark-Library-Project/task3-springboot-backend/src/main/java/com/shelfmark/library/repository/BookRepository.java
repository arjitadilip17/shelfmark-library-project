package com.shelfmark.library.repository;

import com.shelfmark.library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Plain JDBC data access using Spring's JdbcTemplate - this is the layer the task
 * means by "Database Connectivity using JDBC". Every method writes its own SQL.
 */
@Repository
public class BookRepository {

    private final JdbcTemplate jdbc;

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("category"),
            rs.getString("call_no"),
            rs.getInt("copies")
    );

    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books ORDER BY title", ROW_MAPPER);
    }

    public Optional<Book> findById(Long id) {
        List<Book> result = jdbc.query("SELECT * FROM books WHERE id = ?", ROW_MAPPER, id);
        return result.stream().findFirst();
    }

    public List<Book> search(String query, String category) {
        String like = "%" + query.toLowerCase() + "%";
        if (category == null || category.isBlank() || category.equalsIgnoreCase("All categories")) {
            return jdbc.query(
                    "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? ORDER BY title",
                    ROW_MAPPER, like, like
            );
        }
        return jdbc.query(
                "SELECT * FROM books WHERE (LOWER(title) LIKE ? OR LOWER(author) LIKE ?) AND category = ? ORDER BY title",
                ROW_MAPPER, like, like, category
        );
    }

    public Book save(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO books (title, author, category, call_no, copies) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setString(4, book.getCallNo());
            ps.setInt(5, book.getCopies());
            return ps;
        }, keyHolder);
        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    public int update(Long id, Book book) {
        return jdbc.update(
                "UPDATE books SET title = ?, author = ?, category = ?, call_no = ?, copies = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getCategory(), book.getCallNo(), book.getCopies(), id
        );
    }

    public int updateCopies(Long id, int delta) {
        return jdbc.update(
                "UPDATE books SET copies = copies + ? WHERE id = ? AND copies + ? >= 0",
                delta, id, delta
        );
    }

    public int deleteById(Long id) {
        return jdbc.update("DELETE FROM books WHERE id = ?", id);
    }
}
