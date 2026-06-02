package com.gmh.cricket_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String sessionToken;
    private String userId;
    private String username;
}

