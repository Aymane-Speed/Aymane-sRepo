package com.example.ebank.repository;

import com.example.ebank.models.BankAccount;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByRib(String rib);
    boolean existsByRib(String rib);

    @Query("select a from BankAccount a where a.owner.id = :clientId order by a.updatedAt desc")
    List<BankAccount> findByClientIdOrderByUpdatedAtDesc(Long clientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from BankAccount a where a.rib = :rib")
    Optional<BankAccount> findByRibForUpdate(String rib);
}
