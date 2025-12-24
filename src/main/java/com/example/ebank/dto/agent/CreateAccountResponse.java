package com.example.ebank.dto.agent;

public class CreateAccountResponse {
    private Long accountId;
    private String rib;
    private String status;

    public CreateAccountResponse(Long accountId, String rib, String status) {
        this.accountId = accountId;
        this.rib = rib;
        this.status = status;
    }

    public Long getAccountId() { return accountId; }
    public String getRib() { return rib; }
    public String getStatus() { return status; }
}
