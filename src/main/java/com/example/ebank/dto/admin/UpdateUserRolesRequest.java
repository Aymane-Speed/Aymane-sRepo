package com.example.ebank.dto.admin;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateUserRolesRequest {
    @NotEmpty
    private List<String> roles;

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
