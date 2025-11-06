package com.invenza.invenza_backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invenza.invenza_backend.dto.LoginRequest;
import com.invenza.invenza_backend.service.LoginService;
import com.invenza.invenza_backend.dto.AuthResponseDto;


@RestController
@RequestMapping("/api")
public class LoginPage {
    @Autowired
    private LoginService loginService;

 @Autowired
private AuthenticationManager authenticationManager;
@Autowired
private PasswordEncoder passwordEncoder;

@PostMapping("/auth/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
     System.out.println("Attempting login for: " + request.getUsername()); 

    try {
        Authentication auth = authenticationManager.authenticate(
            
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        
            );

        String token = loginService.generateToken(request.getUsername());
       return ResponseEntity.ok(new AuthResponseDto(token));
        
    } catch (AuthenticationException e) {
         System.out.println("AuthenticationException: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

}
 @GetMapping("/encode")
public ResponseEntity<String> encodePassword(@RequestParam String raw) {
    String encoded = passwordEncoder.encode(raw);
    return ResponseEntity.ok("Encoded password: " + encoded);
}

}
