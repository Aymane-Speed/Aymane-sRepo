package com.example.ebank.service.interfaces;

import com.example.ebank.dto.admin.RoleDto;
import com.example.ebank.dto.client.AccountDTO;
import com.example.ebank.dto.client.DashboardResponse;
import com.example.ebank.dto.client.TransferRequest;
import com.example.ebank.dto.client.TransferResponse;
import com.example.ebank.models.BankAccount;
import com.example.ebank.security.UserPrincipal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientService {
    List<AccountDTO> listMyAccounts();
    public DashboardResponse getDashboard(String rib, int page, int size);
    public TransferResponse transfer(TransferRequest req);
    public BankAccount resolveSelectedAccount(List<BankAccount> accounts, String rib);
    public UserPrincipal requireClient();
}
