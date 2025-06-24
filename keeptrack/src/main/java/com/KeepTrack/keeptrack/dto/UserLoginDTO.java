package com.KeepTrack.keeptrack.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
    private String role; 
}
