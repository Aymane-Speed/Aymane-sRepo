package com.example.ebank.security;

import com.example.ebank.models.AppUser;
import com.example.ebank.repository.AppUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long clientId = (u.getClient() != null) ? u.getClient().getId() : null;
        List<String> roles = u.getRoles().stream().map(r -> r.getName()).toList();

        return new UserPrincipal(u.getId(), clientId, u.getUsername(), u.getPasswordHash(), roles, u.isEnabled());
    }
}
