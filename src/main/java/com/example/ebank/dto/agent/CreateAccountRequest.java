package com.example.ebank.dto.agent;

import jakarta.validation.constraints.NotBlank;

public class CreateAccountRequest {

    @NotBlank
    private String rib;

    @NotBlank
    private String clientNationalId;

    public String getRib() { return rib; }
    public void setRib(String rib) { this.rib = rib; }

    public String getClientNationalId() { return clientNationalId; }
    public void setClientNationalId(String clientNationalId) { this.clientNationalId = clientNationalId; }
}
