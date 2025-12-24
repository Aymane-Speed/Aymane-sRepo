package com.example.ebank.dto.client;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank
    private String fromRib;

    @NotNull
    @DecimalMin(value = "0.01", message = "Le montant doit être > 0")
    private BigDecimal amount;

    @NotBlank
    private String toRib;

    @NotBlank
    @Size(max = 140)
    private String motif;

    public String getFromRib() { return fromRib; }
    public void setFromRib(String fromRib) { this.fromRib = fromRib; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getToRib() { return toRib; }
    public void setToRib(String toRib) { this.toRib = toRib; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
