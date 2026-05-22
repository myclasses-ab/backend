package com.classes.Backend.dto.auth;

import com.classes.Backend.Domain.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private UserRole role;
}
