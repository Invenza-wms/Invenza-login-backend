package com.invenza.invenza_backend.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invenza.invenza_backend.entity.LoginUsers;



public interface LoginRepository extends JpaRepository<LoginUsers, String> {
    // Optional: custom query methods
     Optional <LoginUsers> findByUsername(String username);

        
    
}
