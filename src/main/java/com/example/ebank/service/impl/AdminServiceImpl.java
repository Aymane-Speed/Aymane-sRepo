package com.example.ebank.service.impl;

import com.example.ebank.models.AppUser;
import com.example.ebank.models.Role;
import com.example.ebank.dto.admin.*;
import com.example.ebank.exception.BusinessException;
import com.example.ebank.repository.AppUserRepository;
import com.example.ebank.repository.RoleRepository;
import com.example.ebank.service.interfaces.AdminService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepo;
    private final AppUserRepository userRepo;

    public AdminServiceImpl(RoleRepository roleRepo, AppUserRepository userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<RoleDto> listRoles() {
        return roleRepo.findAll(Sort.by("name")).stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .toList();
    }

    @Transactional
    public RoleDto createRole(String nameRaw) {
        String name = normalizeRole(nameRaw);
        if (roleRepo.existsByName(name)) {
            throw BusinessException.badRequest("Role existe déjà");
        }
        Role saved = roleRepo.save(new Role(name));
        return new RoleDto(saved.getId(), saved.getName());
    }

    @Transactional
    public void deleteRole(Long roleId) {
        long usersWithRole = userRepo.countByRoles_Id(roleId);
        if (usersWithRole > 0) {
            throw BusinessException.badRequest("Impossible de supprimer un role déjà attribué à des utilisateurs");
        }
        if (!roleRepo.existsById(roleId)) {
            throw BusinessException.notFound("Role introuvable");
        }
        roleRepo.deleteById(roleId);
    }

    @Transactional(readOnly = true)
    public List<UserAdminDto> listUsers() {
        return userRepo.findAll(Sort.by("id").ascending()).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserAdminDto getUser(Long userId) {
        AppUser u = userRepo.findById(userId).orElseThrow(() -> BusinessException.notFound("Utilisateur introuvable"));
        return toDto(u);
    }

    @Transactional
    public UserAdminDto setUserRoles(Long userId, List<String> roleNamesRaw) {
        AppUser u = userRepo.findById(userId).orElseThrow(() -> BusinessException.notFound("Utilisateur introuvable"));

        Set<Role> roles = new HashSet<>();
        for (String raw : roleNamesRaw) {
            String name = normalizeRole(raw);
            Role role = roleRepo.findByName(name).orElseThrow(() -> BusinessException.badRequest("Role inexistant: " + name));
            roles.add(role);
        }

        u.setRoles(roles);
        userRepo.save(u);
        return toDto(u);
    }

    private UserAdminDto toDto(AppUser u) {
        Long clientId = u.getClient() == null ? null : u.getClient().getId();
        List<String> roles = u.getRoles().stream().map(Role::getName).sorted().toList();
        return new UserAdminDto(u.getId(), u.getUsername(), u.isEnabled(), clientId, roles);
    }

    private String normalizeRole(String raw) {
        if (raw == null) throw BusinessException.badRequest("Role invalide");
        String name = raw.trim().toUpperCase(Locale.ROOT);
        if (name.isBlank()) throw BusinessException.badRequest("Role invalide");
        if (!name.matches("[A-Z0-9_]{2,50}")) {
            throw BusinessException.badRequest("Nom de role invalide (utiliser A-Z, 0-9, _)");
        }
        return name;
    }
}
