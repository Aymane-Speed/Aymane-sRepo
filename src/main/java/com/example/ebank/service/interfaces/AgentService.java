package com.example.ebank.service.interfaces;

import com.example.ebank.dto.agent.CreateAccountRequest;
import com.example.ebank.dto.agent.CreateAccountResponse;
import com.example.ebank.dto.agent.CreateClientRequest;
import com.example.ebank.dto.agent.CreateClientResponse;

public interface AgentService {
    public CreateClientResponse createClient(CreateClientRequest req);
    public CreateAccountResponse createAccount(CreateAccountRequest req);


    }
