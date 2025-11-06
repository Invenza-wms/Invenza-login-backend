package com.invenza.invenza_backend.dto;

public class AuthResponseDto {
    private String token;
    private String username;
    private String role;

    // No-arg constructor (needed by Jackson / frameworks)
    public AuthResponseDto() {}

    // All-args constructor (what your controller uses)
    public AuthResponseDto(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Convenience constructor (single-arg) in case some code still uses it
    public AuthResponseDto(String token) {
        this.token = token;
    }

    // Getters & setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
