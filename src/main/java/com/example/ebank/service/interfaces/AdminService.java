package com.example.ebank.service.interfaces;

import com.example.ebank.dto.admin.RoleDto;
import com.example.ebank.dto.admin.UserAdminDto;

import java.util.List;

public interface AdminService {
    public List<RoleDto> listRoles();
    public RoleDto createRole(String nameRaw);
    public void deleteRole(Long roleId);
    public List<UserAdminDto> listUsers();
    public UserAdminDto getUser(Long userId);
    public UserAdminDto setUserRoles(Long userId, List<String> roleNamesRaw);

    }
