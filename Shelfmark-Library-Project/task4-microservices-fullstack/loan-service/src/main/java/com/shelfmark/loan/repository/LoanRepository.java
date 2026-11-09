package com.shelfmark.loan.repository;

import com.shelfmark.loan.model.Loan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class LoanRepository {
    private final JdbcTemplate jdbc;

    public LoanRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Loan> mapper = (rs, rowNum) -> new Loan(
            rs.getLong("id"),
            rs.getLong("book_id"),
            rs.getLong("member_id"),
            rs.getDate("borrowed_on").toLocalDate(),
            rs.getDate("due_date").toLocalDate(),
            rs.getDate("returned_on") != null ? rs.getDate("returned_on").toLocalDate() : null,
            rs.getBigDecimal("fine_amount") != null ? rs.getBigDecimal("fine_amount") : BigDecimal.ZERO
    );

    public List<Loan> findAllActive() {
        return jdbc.query("SELECT * FROM loans WHERE returned_on IS NULL ORDER BY id DESC", mapper);
    }

    public List<Loan> findActiveByMemberId(Long memberId) {
        return jdbc.query("SELECT * FROM loans WHERE member_id = ? AND returned_on IS NULL ORDER BY id DESC", mapper, memberId);
    }

    public Optional<Loan> findActiveLoan(Long bookId, Long memberId) {
        List<Loan> list = jdbc.query(
                "SELECT * FROM loans WHERE book_id = ? AND member_id = ? AND returned_on IS NULL",
                mapper, bookId, memberId
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<Loan> findById(Long id) {
        List<Loan> list = jdbc.query("SELECT * FROM loans WHERE id = ?", mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Loan createLoan(Long bookId, Long memberId, int loanDays) {
        LocalDate now = LocalDate.now();
        LocalDate due = now.plusDays(loanDays);
        jdbc.update("INSERT INTO loans (book_id, member_id, borrowed_on, due_date) VALUES (?, ?, ?, ?)",
                bookId, memberId, now, due);
        Long id = jdbc.queryForObject("SELECT MAX(id) FROM loans", Long.class);
        return new Loan(id, bookId, memberId, now, due, null, BigDecimal.ZERO);
    }

    public void markReturned(Long id, BigDecimal fine) {
        jdbc.update("UPDATE loans SET returned_on = CURRENT_DATE, fine_amount = ? WHERE id = ?", fine, id);
    }
}
