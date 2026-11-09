package com.shelfmark.member.controller;

import com.shelfmark.member.model.Member;
import com.shelfmark.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberRepository repo;

    public MemberController(MemberRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Member> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member create(@RequestBody Member member) {
        return repo.save(member);
    }
}
