package com.invenza.invenza_backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserSettingsDto {
     @NotBlank(message = "Name is required")
    private String username;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Current password is required")
    private String currentPassword;
    
    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;
    
    private String language;
    
  //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}