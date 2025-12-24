package com.example.ebank.service.interfaces;

import com.example.ebank.dto.auth.AuthRequest;
import com.example.ebank.dto.auth.AuthResponse;
import com.example.ebank.dto.auth.ChangePasswordRequest;

public interface AuthService {
    public AuthResponse login(AuthRequest req);
    public void changePassword(ChangePasswordRequest req);
}
