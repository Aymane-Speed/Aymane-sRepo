package com.example.ebank.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "operations", indexes = {
        @Index(name = "idx_ops_account_date", columnList = "account_id, occurredAt")
})
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_operation_account"))
    private BankAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OperationType type;

    @Column(nullable = false, length = 255)
    private String label;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false, length = 64)
    private String referenceId; // same for debit+credit of one transfer

    public Operation() {}

    public Operation(BankAccount account, OperationType type, String label, BigDecimal amount, Instant occurredAt, String referenceId) {
        this.account = account;
        this.type = type;
        this.label = label;
        this.amount = amount;
        this.occurredAt = occurredAt;
        this.referenceId = referenceId;
    }

    // getters
    public Long getId() { return id; }
    public BankAccount getAccount() { return account; }
    public OperationType getType() { return type; }
    public String getLabel() { return label; }
    public BigDecimal getAmount() { return amount; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getReferenceId() { return referenceId; }
}
