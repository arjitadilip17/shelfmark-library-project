package com.shelfmark.library.controller;

import com.shelfmark.library.model.Member;
import com.shelfmark.library.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberRepository members;

    public MemberController(MemberRepository members) {
        this.members = members;
    }

    @GetMapping
    public List<Member> list() {
        return members.findAll();
    }

    // Called by the React registration form (RegisterForm.jsx)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member register(@Valid @RequestBody Member member) {
        return members.save(member);
    }
}
