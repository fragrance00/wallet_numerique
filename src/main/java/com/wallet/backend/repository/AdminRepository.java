package com.wallet.backend.repository;

import com.wallet.backend.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// AdminRepository.java
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}