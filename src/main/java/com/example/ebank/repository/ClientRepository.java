package com.example.ebank.repository;

import com.example.ebank.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNationalId(String nationalId);
    Optional<Client> findByEmail(String email);
    boolean existsByNationalId(String nationalId);
    boolean existsByEmail(String email);
}
