package com.example.ebank.dto.admin;

import jakarta.validation.constraints.NotBlank;

public class CreateRoleRequest {
    @NotBlank
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
