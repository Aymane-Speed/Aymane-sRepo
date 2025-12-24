package com.example.ebank.dto.client;

import java.time.Instant;

public class TransferResponse {
    private String referenceId;
    private Long debitOperationId;
    private Long creditOperationId;
    private Instant timestamp;

    public TransferResponse(String referenceId, Long debitOperationId, Long creditOperationId, Instant timestamp) {
        this.referenceId = referenceId;
        this.debitOperationId = debitOperationId;
        this.creditOperationId = creditOperationId;
        this.timestamp = timestamp;
    }

    public String getReferenceId() { return referenceId; }
    public Long getDebitOperationId() { return debitOperationId; }
    public Long getCreditOperationId() { return creditOperationId; }
    public Instant getTimestamp() { return timestamp; }
}
