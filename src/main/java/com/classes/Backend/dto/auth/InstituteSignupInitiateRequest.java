package com.classes.Backend.dto.auth;

import lombok.Data;

@Data
public class InstituteSignupInitiateRequest {
    private String email;
    private String phone;
    private String password;
    private String instituteName;
}
