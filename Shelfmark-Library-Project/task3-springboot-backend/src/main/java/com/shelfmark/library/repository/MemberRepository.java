package com.shelfmark.library.repository;

import com.shelfmark.library.model.Member;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbc;

    public MemberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("plan"),
            rs.getDate("joined_on").toLocalDate()
    );

    public List<Member> findAll() {
        return jdbc.query("SELECT * FROM members ORDER BY joined_on DESC", ROW_MAPPER);
    }

    /** @throws DuplicateKeyException if the email already exists (caught by the controller) */
    public Member save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO members (name, email, phone, plan) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getPlan());
            return ps;
        }, keyHolder);
        member.setId(keyHolder.getKey().longValue());
        return member;
    }
}
