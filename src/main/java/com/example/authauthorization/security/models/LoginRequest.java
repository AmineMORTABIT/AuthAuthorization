package com.example.authauthorization.security.models;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}
