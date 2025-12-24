package com.example.ebank.dto.admin;

import java.util.List;

public class UserAdminDto {
    private Long id;
    private String username;
    private boolean enabled;
    private Long clientId;
    private List<String> roles;

    public UserAdminDto(Long id, String username, boolean enabled, Long clientId, List<String> roles) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.clientId = clientId;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public boolean isEnabled() { return enabled; }
    public Long getClientId() { return clientId; }
    public List<String> getRoles() { return roles; }
}
