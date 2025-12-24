package com.example.ebank.web;

import com.example.ebank.dto.client.*;
import com.example.ebank.service.impl.ClientServiceImpl;
import com.example.ebank.service.interfaces.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> myAccounts() {
        return ResponseEntity.ok(clientService.listMyAccounts());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard(
            @RequestParam(name = "rib", required = false) String rib,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(clientService.getDashboard(rib, page, size));
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest req) {
        return ResponseEntity.ok(clientService.transfer(req));
    }
}
