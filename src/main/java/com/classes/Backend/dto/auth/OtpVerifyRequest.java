package com.classes.Backend.dto.auth;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String phone;
    private String otp;
    private String fullName;
}
