package com.example.ebank.dto.client;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private List<AccountDTO> accounts;
    private String selectedRib;
    private BigDecimal balance;
    private int page;
    private int size;
    private long totalOperations;
    private List<OperationDTO> operations;

    public DashboardResponse(List<AccountDTO> accounts, String selectedRib, BigDecimal balance,
                             int page, int size, long totalOperations, List<OperationDTO> operations) {
        this.accounts = accounts;
        this.selectedRib = selectedRib;
        this.balance = balance;
        this.page = page;
        this.size = size;
        this.totalOperations = totalOperations;
        this.operations = operations;
    }

    public List<AccountDTO> getAccounts() { return accounts; }
    public String getSelectedRib() { return selectedRib; }
    public BigDecimal getBalance() { return balance; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalOperations() { return totalOperations; }
    public List<OperationDTO> getOperations() { return operations; }
}
