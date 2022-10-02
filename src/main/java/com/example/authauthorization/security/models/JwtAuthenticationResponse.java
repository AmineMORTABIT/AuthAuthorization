package com.example.authauthorization.security.models;

import org.springframework.security.core.userdetails.User;

import java.util.Map;

public class JwtAuthenticationResponse {

    private SecurityUser user;
    private Map<String,String> Tokens;

    public JwtAuthenticationResponse(SecurityUser user, Map<String, String> Tokens) {
        this.user = user;
        this.Tokens = Tokens;
    }


    public Map<String, String> getTokens() {
        return Tokens;
    }

    public void setTokens(Map<String, String> tokens) {
        Tokens = tokens;
    }

    public SecurityUser getCurrentUser() {
        return user;
    }

    public void setCurrentUser(SecurityUser currentUser) {
        this.user = currentUser;
    }
}
