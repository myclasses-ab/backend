package com.classes.Backend.dto.users;

import lombok.Data;

@Data
public class UserActivityTrackRequest {
    private String city;
    private String exam;
    private String instituteIdentifier;
    private String instituteName;
}
