package com.example.ebank.web;

import com.example.ebank.dto.admin.*;
import com.example.ebank.service.impl.AdminServiceImpl;
import com.example.ebank.service.interfaces.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    // ---- Roles ----

    @GetMapping("/roles")
    public List<RoleDto> listRoles() {
        return adminService.listRoles();
    }

    @PostMapping("/roles")
    public RoleDto createRole(@Valid @RequestBody CreateRoleRequest req) {
        return adminService.createRole(req.getName());
    }

    @DeleteMapping("/roles/{roleId}")
    public void deleteRole(@PathVariable Long roleId) {
        adminService.deleteRole(roleId);
    }

    // ---- Users / role assignment ----

    @GetMapping("/users")
    public List<UserAdminDto> listUsers() {
        return adminService.listUsers();
    }

    @GetMapping("/users/{userId}")
    public UserAdminDto getUser(@PathVariable Long userId) {
        return adminService.getUser(userId);
    }

    @PutMapping("/users/{userId}/roles")
    public UserAdminDto setUserRoles(@PathVariable Long userId,
                                     @Valid @RequestBody UpdateUserRolesRequest req) {
        return adminService.setUserRoles(userId, req.getRoles());
    }


}
