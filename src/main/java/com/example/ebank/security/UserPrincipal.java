package com.example.ebank.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final Long clientId; // nullable
    private final String username;
    private final String passwordHash;
    private final List<String> roles; // e.g. CLIENT, AGENT_GUICHET
    private final boolean enabled;

    public UserPrincipal(Long userId, Long clientId, String username, String passwordHash, List<String> roles, boolean enabled) {
        this.userId = userId;
        this.clientId = clientId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.enabled = enabled;
    }

    public Long getUserId() { return userId; }
    public Long getClientId() { return clientId; }
    public List<String> getRoles() { return roles; }

    public boolean hasRole(String roleName) {
        return roles != null && roles.stream().filter(Objects::nonNull).anyMatch(r -> r.equalsIgnoreCase(roleName));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return List.of();
        return roles.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return passwordHash; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}
