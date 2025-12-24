package com.example.ebank.dto.client;

import java.math.BigDecimal;

public class AccountDTO {
    private String rib;
    private String status;
    private BigDecimal balance;

    public AccountDTO(String rib, String status, BigDecimal balance) {
        this.rib = rib;
        this.status = status;
        this.balance = balance;
    }

    public String getRib() { return rib; }
    public String getStatus() { return status; }
    public BigDecimal getBalance() { return balance; }
}
