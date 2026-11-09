package com.shelfmark.library.repository;

import com.shelfmark.library.model.Loan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LoanRepository {

    private final JdbcTemplate jdbc;

    public LoanRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // JOINs in the book title and member name so the frontend doesn't need extra calls
    private static final RowMapper<Loan> ROW_MAPPER = (rs, rowNum) -> {
        Loan loan = new Loan(
                rs.getLong("id"),
                rs.getLong("book_id"),
                rs.getLong("member_id"),
                rs.getDate("borrowed_on").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                rs.getDate("returned_on") == null ? null : rs.getDate("returned_on").toLocalDate()
        );
        loan.setBookTitle(rs.getString("book_title"));
        loan.setMemberName(rs.getString("member_name"));
        return loan;
    };

    private static final String SELECT_BASE =
            "SELECT l.*, b.title AS book_title, m.name AS member_name " +
            "FROM loans l JOIN books b ON l.book_id = b.id JOIN members m ON l.member_id = m.id ";

    public List<Loan> findActive() {
        return jdbc.query(SELECT_BASE + "WHERE l.returned_on IS NULL ORDER BY l.due_date", ROW_MAPPER);
    }

    public List<Loan> findByMember(Long memberId) {
        return jdbc.query(SELECT_BASE + "WHERE l.member_id = ? ORDER BY l.borrowed_on DESC", ROW_MAPPER, memberId);
    }

    public Loan save(Long bookId, Long memberId, int loanDays) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDate due = LocalDate.now().plusDays(loanDays);
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO loans (book_id, member_id, borrowed_on, due_date) VALUES (?, ?, CURRENT_DATE, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, bookId);
            ps.setLong(2, memberId);
            ps.setObject(3, due);
            return ps;
        }, keyHolder);

        Loan loan = new Loan(keyHolder.getKey().longValue(), bookId, memberId, LocalDate.now(), due, null);
        return loan;
    }

    public int markReturned(Long loanId) {
        return jdbc.update(
                "UPDATE loans SET returned_on = CURRENT_DATE WHERE id = ? AND returned_on IS NULL",
                loanId
        );
    }
}
