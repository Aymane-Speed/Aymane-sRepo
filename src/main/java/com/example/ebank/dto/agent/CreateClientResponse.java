package com.example.ebank.dto.agent;

public class CreateClientResponse {
    private Long clientId;
    private String login;

    public CreateClientResponse(Long clientId, String login) {
        this.clientId = clientId;
        this.login = login;
    }

    public Long getClientId() { return clientId; }
    public String getLogin() { return login; }
}
