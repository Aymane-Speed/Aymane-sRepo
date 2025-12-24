package com.example.ebank.dto.client;

import java.math.BigDecimal;
import java.time.Instant;

public class OperationDTO {
    private Long id;
    private String label;
    private String type;
    private Instant date;
    private BigDecimal amount;

    public OperationDTO(Long id, String label, String type, Instant date, BigDecimal amount) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public String getLabel() { return label; }
    public String getType() { return type; }
    public Instant getDate() { return date; }
    public BigDecimal getAmount() { return amount; }
}
