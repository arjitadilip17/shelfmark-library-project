package com.shelfmark.library.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class Member {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Plan is required")
    private String plan;

    private LocalDate joinedOn;

    public Member() {
    }

    public Member(Long id, String name, String email, String phone, String plan, LocalDate joinedOn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.plan = plan;
        this.joinedOn = joinedOn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public LocalDate getJoinedOn() { return joinedOn; }
    public void setJoinedOn(LocalDate joinedOn) { this.joinedOn = joinedOn; }
}
