package com.invenza.invenza_backend.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.invenza.invenza_backend.entity.LoginUsers;
import com.invenza.invenza_backend.repository.LoginRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class LoginService implements UserDetailsService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    public LoginService(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    public boolean validatePassword(String username, String rawPassword) {
        return loginRepository.findByUsername(username)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
    }

    public void updatePassword(String username, String newEncodedPassword) {
        LoginUsers user = loginRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(newEncodedPassword);
        loginRepository.save(user);
    }

    public boolean authenticate(String username, String rawPassword) {
        return loginRepository.findByUsername(username)
            .map(user -> {
            System.out.println("User found: " + user.getUsername());
    System.out.println("Stored password: " + user.getPassword());
    boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
    return matches;
        })
        .orElseGet(() -> {
            System.out.println("User not found: " + username);
            return false;
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         System.out.println("Loading user for authentication: " + username);
        LoginUsers user = loginRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User loaded: " + user.getUsername());
        System.out.println("Encoded password: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
