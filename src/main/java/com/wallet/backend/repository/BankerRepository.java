package com.wallet.backend.repository;

import com.wallet.backend.entities.Banker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BankerRepository extends JpaRepository<Banker, Long> {
    Optional<Banker> findByUsername(String username);
    Optional<Banker> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}