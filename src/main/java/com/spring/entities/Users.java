package com.spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Phone", nullable = false)
    private String phone;

    @Column(name = "Password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
