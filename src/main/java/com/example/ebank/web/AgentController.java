package com.example.ebank.web;

import com.example.ebank.dto.agent.*;
import com.example.ebank.service.interfaces.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class AgentController{

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/clients")
    public ResponseEntity<CreateClientResponse> createClient(@Valid @RequestBody CreateClientRequest req) {
        return ResponseEntity.ok(agentService.createClient(req));
    }

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest req) {
        return ResponseEntity.ok(agentService.createAccount(req));
    }
}
