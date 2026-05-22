package com.classes.Backend.dto.auth;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String instituteName;
}
