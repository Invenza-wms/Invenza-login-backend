package com.invenza.invenza_backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invenza.invenza_backend.dto.LoginRequest;
import com.invenza.invenza_backend.service.LoginService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class LoginPage {
    @Autowired
    private LoginService loginService;

    @PostMapping("/auth/login")

 public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
         System.out.println("Login endpoint hit");

        boolean success = loginService.authenticate(request.getUsername(), request.getPassword());
        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials or user not registered. Please sign up.");
        }
        return ResponseEntity.ok("Login successful");
    }

}

