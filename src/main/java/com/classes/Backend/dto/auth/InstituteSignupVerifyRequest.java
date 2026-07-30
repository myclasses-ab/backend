package com.classes.Backend.dto.auth;

import lombok.Data;

@Data
public class InstituteSignupVerifyRequest {
    private String email;
    private String code;
}
