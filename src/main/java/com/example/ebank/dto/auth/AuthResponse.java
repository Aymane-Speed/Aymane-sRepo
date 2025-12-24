package com.example.ebank.dto.auth;

import java.time.Instant;
import java.util.List;

public class AuthResponse {
    private String token;
    private List<String> roles;
    private Instant expiresAt;

    public AuthResponse(String token, List<String> roles, Instant expiresAt) {
        this.token = token;
        this.roles = roles;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public List<String> getRoles() { return roles; }
    public Instant getExpiresAt() { return expiresAt; }
}
