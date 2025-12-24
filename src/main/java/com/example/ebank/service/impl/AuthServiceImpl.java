package com.example.ebank.service.impl;

import com.example.ebank.models.AppUser;
import com.example.ebank.dto.auth.*;
import com.example.ebank.exception.BusinessException;
import com.example.ebank.repository.AppUserRepository;
import com.example.ebank.security.JwtService;
import com.example.ebank.security.UserPrincipal;
import com.example.ebank.service.interfaces.AuthService;
import com.example.ebank.util.SecurityUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AppUserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(AuthenticationManager authManager,
                           JwtService jwtService,
                           AppUserRepository userRepository,
                           PasswordEncoder encoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public AuthResponse login(AuthRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getLogin(), req.getPassword())
            );

            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            String token = jwtService.generateToken(principal);
            Instant expiresAt = Instant.now().plusSeconds(jwtService.getTtlSeconds());
            return new AuthResponse(token, principal.getRoles(), expiresAt);
        } catch (BadCredentialsException ex) {
            throw BusinessException.unauthorized("Login ou mot de passe erronés");
        }
    }

    public void changePassword(ChangePasswordRequest req) {
        UserPrincipal p = SecurityUtils.principal();
        if (p == null) throw new BadCredentialsException("Not authenticated");

        AppUser u = userRepository.findByUsername(p.getUsername())
                .orElseThrow(() -> BusinessException.notFound("Utilisateur introuvable"));

        if (!encoder.matches(req.getOldPassword(), u.getPasswordHash())) {
            throw BusinessException.badRequest("Ancien mot de passe erroné");
        }

        u.setPasswordHash(encoder.encode(req.getNewPassword()));
        userRepository.save(u);
    }
}
