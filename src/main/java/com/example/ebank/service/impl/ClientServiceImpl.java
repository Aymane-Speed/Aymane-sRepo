package com.example.ebank.service.impl;

import com.example.ebank.dto.admin.RoleDto;
import com.example.ebank.models.*;
import com.example.ebank.dto.client.*;
import com.example.ebank.exception.BusinessException;
import com.example.ebank.repository.*;
import com.example.ebank.security.UserPrincipal;
import com.example.ebank.service.interfaces.ClientService;
import com.example.ebank.util.SecurityUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final BankAccountRepository accountRepo;
    private final OperationRepository operationRepo;

    public ClientServiceImpl(BankAccountRepository accountRepo, OperationRepository operationRepo) {
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public List<AccountDTO> listMyAccounts() {
        UserPrincipal p = requireClient();
        List<BankAccount> accounts = accountRepo.findByClientIdOrderByUpdatedAtDesc(p.getClientId());
        return accounts.stream()
                .map(a -> new AccountDTO(a.getRib(), a.getStatus().name(), a.getBalance()))
                .toList();
    }

    public DashboardResponse getDashboard(String rib, int page, int size) {
        UserPrincipal p = requireClient();

        List<BankAccount> accounts = accountRepo.findByClientIdOrderByUpdatedAtDesc(p.getClientId());
        if (accounts.isEmpty()) {
            return new DashboardResponse(List.of(), null, BigDecimal.ZERO, page, size, 0, List.of());
        }

        BankAccount selected = resolveSelectedAccount(accounts, rib);

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50), Sort.by(Sort.Direction.DESC, "occurredAt"));
        Page<Operation> opsPage = operationRepo.findByAccountOrderByOccurredAtDesc(selected, pageable);

        List<AccountDTO> accountDtos = accounts.stream()
                .map(a -> new AccountDTO(a.getRib(), a.getStatus().name(), a.getBalance()))
                .collect(Collectors.toList());

        List<OperationDTO> opDtos = opsPage.getContent().stream()
                .map(o -> new OperationDTO(o.getId(), o.getLabel(), o.getType().name(), o.getOccurredAt(), o.getAmount()))
                .toList();

        return new DashboardResponse(accountDtos, selected.getRib(), selected.getBalance(),
                opsPage.getNumber(), opsPage.getSize(), opsPage.getTotalElements(), opDtos);
    }

    @Transactional
    public TransferResponse transfer(TransferRequest req) {
        UserPrincipal p = requireClient();

        String fromRib = req.getFromRib().trim();
        String toRib = req.getToRib().trim();

        if (fromRib.equals(toRib)) {
            throw BusinessException.badRequest("Le RIB destinataire doit être différent");
        }

        // Lock accounts in deterministic order to reduce deadlocks
        String first = fromRib.compareTo(toRib) < 0 ? fromRib : toRib;
        String second = fromRib.compareTo(toRib) < 0 ? toRib : fromRib;

        BankAccount a1 = accountRepo.findByRibForUpdate(first)
                .orElseThrow(() -> BusinessException.notFound("Compte introuvable: " + first));
        BankAccount a2 = accountRepo.findByRibForUpdate(second)
                .orElseThrow(() -> BusinessException.notFound("Compte introuvable: " + second));

        BankAccount from = fromRib.equals(first) ? a1 : a2;
        BankAccount to = toRib.equals(first) ? a1 : a2;

        // Ensure the source account belongs to this client
        if (!Objects.equals(from.getOwner().getId(), p.getClientId())) {
            throw BusinessException.badRequest("Compte source invalide");
        }

        // RG_11
        if (from.getStatus() == AccountStatus.BLOQUE || from.getStatus() == AccountStatus.CLOTURE) {
            throw BusinessException.badRequest("Le compte bancaire ne doit pas être bloqué ou clôturé");
        }

        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw BusinessException.badRequest("Montant invalide");
        }

        // RG_12: balance must be > amount
        if (from.getBalance().compareTo(amount) <= 0) {
            throw BusinessException.badRequest("Le solde de compte doit être supérieur au montant du virement");
        }

        // Perform transfer (RG_13, RG_14)
        from.setBalance(from.getBalance().subtract(amount));
        from.touch();

        to.setBalance(to.getBalance().add(amount));
        to.touch();

        String ref = UUID.randomUUID().toString().replace("-", "");
        Instant now = Instant.now();

        String debitLabel = "Virement vers " + toRib + " - " + req.getMotif();
        String creditLabel = "Virement en votre faveur de " + fromRib + " - " + req.getMotif();

        Operation debit = new Operation(from, OperationType.DEBIT, debitLabel, amount, now, ref);
        Operation credit = new Operation(to, OperationType.CREDIT, creditLabel, amount, now, ref);

        operationRepo.save(debit);
        operationRepo.save(credit);

        accountRepo.save(from);
        accountRepo.save(to);

        // RG_15 traced by operations with date `now`
        return new TransferResponse(ref, debit.getId(), credit.getId(), now);
    }

    public BankAccount resolveSelectedAccount(List<BankAccount> accounts, String rib) {
        if (rib == null || rib.isBlank()) {
            return accounts.get(0); // already sorted by updatedAt desc
        }
        for (BankAccount a : accounts) {
            if (a.getRib().equals(rib.trim())) return a;
        }
        throw BusinessException.badRequest("Ce compte ne vous appartient pas");
    }

    public UserPrincipal requireClient() {
        UserPrincipal p = SecurityUtils.principal();
        if (p == null || p.getClientId() == null) {
            throw BusinessException.unauthorized("Session invalide, veuillez s’authentifier");
        }
        return p;
    }
}
