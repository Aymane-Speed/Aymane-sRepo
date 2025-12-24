package com.example.ebank.repository;

import com.example.ebank.models.Operation;
import com.example.ebank.models.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findTop10ByAccountOrderByOccurredAtDesc(BankAccount account);
    Page<Operation> findByAccountOrderByOccurredAtDesc(BankAccount account, Pageable pageable);
}
