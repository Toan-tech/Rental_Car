package com.spring.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsersDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;
    private String role;
}
