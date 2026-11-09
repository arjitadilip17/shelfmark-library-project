package com.shelfmark.member.repository;

import com.shelfmark.member.model.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbc;

    public MemberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Member> mapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("plan"),
            rs.getDate("joined_on").toLocalDate()
    );

    public List<Member> findAll() {
        return jdbc.query("SELECT * FROM members ORDER BY id", mapper);
    }

    public Optional<Member> findById(Long id) {
        List<Member> list = jdbc.query("SELECT * FROM members WHERE id = ?", mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Member save(Member member) {
        LocalDate joined = member.getJoinedOn() != null ? member.getJoinedOn() : LocalDate.now();
        jdbc.update("INSERT INTO members (name, email, phone, plan, joined_on) VALUES (?, ?, ?, ?, ?)",
                member.getName(), member.getEmail(), member.getPhone(), member.getPlan(), joined);
        Long id = jdbc.queryForObject("SELECT MAX(id) FROM members", Long.class);
        member.setId(id);
        member.setJoinedOn(joined);
        return member;
    }
}
