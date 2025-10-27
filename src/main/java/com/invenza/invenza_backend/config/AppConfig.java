package com.invenza.invenza_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.invenza.invenza_backend.entity.LoginUsers;
import com.invenza.invenza_backend.repository.LoginRepository;

@Configuration
public class AppConfig {
    @Bean
public CommandLineRunner seedDefaultUser(LoginRepository loginRepository, PasswordEncoder encoder) {
    return args -> {
        String defaultUsername = "SravaniM";
        String defaultPassword = "Sravani@123";
        if (loginRepository.findByUsername(defaultUsername).isEmpty()) {
            LoginUsers user = new LoginUsers();
            user.setUsername(defaultUsername);
            user.setPassword(encoder.encode(defaultPassword));
            loginRepository.save(user);
            System.out.println("Default user seeded: " + defaultUsername);
        } else {
            System.out.println("Default user already exists: " + defaultUsername);
        }
    };
}

}   





 