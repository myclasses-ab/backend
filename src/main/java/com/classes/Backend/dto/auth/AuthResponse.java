package com.classes.Backend.dto.auth;

import com.classes.Backend.Domain.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
