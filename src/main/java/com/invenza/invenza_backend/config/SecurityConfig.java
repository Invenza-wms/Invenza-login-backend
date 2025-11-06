package com.invenza.invenza_backend.config;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.invenza.invenza_backend.security.JwtAuthenticationFilter;
import com.invenza.invenza_backend.security.JwtService;
import com.invenza.invenza_backend.service.LoginService;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
        @Value("${cors.allowed-origins}")
    private String allowedOrigins;
@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, LoginService loginService) {
    return new JwtAuthenticationFilter(jwtService, loginService);
}

@Bean
public SecurityFilterChain filterChain(
    HttpSecurity http,
    JwtAuthenticationFilter jwtAuthenticationFilter
) throws Exception {
    http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(allowedOrigins.split(","))); // supports multiple origins
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/login", "/api/encode", "/register", "/css/**", "/js/**").permitAll()
            .requestMatchers("/api/user/**").authenticated()
            .anyRequest().authenticated()
        )
      .sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    )
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .permitAll()
        );

    return http.build();
}
@Bean
public AuthenticationManager authenticationManager(LoginService loginService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(loginService); // LoginService must implement UserDetailsService
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
 

