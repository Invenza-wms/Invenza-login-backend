package com.invenza.invenza_backend.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.invenza.invenza_backend.entity.LoginUsers;
import com.invenza.invenza_backend.repository.LoginRepository;

@Service
public class LoginService implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

   public boolean authenticate(String username, String rawPassword) {
    return loginRepository.findByUsername(username)
        .map(user -> {
            System.out.println("Username found: " + user.getUsername());
            System.out.println("Raw password: " + rawPassword);
            System.out.println("Stored hash: " + user.getPassword());
            boolean match = passwordEncoder.matches(rawPassword, user.getPassword());
            System.out.println("Password match: " + match);
            return match;
        })
        .orElseGet(() -> {
            System.out.println("Username not found: " + username);
            return false;
        });
}


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUsers user = loginRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
