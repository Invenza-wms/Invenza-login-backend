package com.invenza.invenza_backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invenza.invenza_backend.dto.UserSettingsDto;
import com.invenza.invenza_backend.service.LoginService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/user")
public class UserProfileController {
@Autowired
private LoginService loginService;
@Autowired
private PasswordEncoder passwordEncoder;
   
@GetMapping("/settings")
public ResponseEntity<UserSettingsDto> getUserSettings() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String loggedInUsername = authentication.getName(); // This gives you the username

    UserSettingsDto dto = new UserSettingsDto();
    dto.setUsername(loggedInUsername);
   String role = authentication.getAuthorities().stream()
    .findFirst()
    .map(grantedAuthority -> grantedAuthority.getAuthority())
    .orElse("USER");
    dto.setRole(role);
    dto.setLanguage("en"); // Optional default
    dto.setCurrentPassword("");
    dto.setNewPassword("");

    return ResponseEntity.ok(dto);
}
@PostMapping("/settings/update")
public ResponseEntity<String> updateUserSettings(@Valid @RequestBody UserSettingsDto dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String loggedInUsername = authentication.getName();

    // Validate current password (pseudo-code)
    boolean isValid = loginService.validatePassword(loggedInUsername, dto.getCurrentPassword());
    if (!isValid) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid current password");
    }

    // Update password
    String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
    loginService.updatePassword(loggedInUsername, encodedNewPassword);

    return ResponseEntity.ok("Settings updated Successfully");

    
}

}