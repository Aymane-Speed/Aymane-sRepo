package com.example.ebank.security;

import com.example.ebank.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long ttlSeconds;

    public JwtService(AppProperties props) {
        this.ttlSeconds = props.getJwt().getTtlSeconds();
        String secretB64 = props.getJwt().getSecretB64();

        if (secretB64 == null || secretB64.isBlank()) {
            byte[] fallback = "DEV_ONLY_CHANGE_ME_32+_BYTES_SECRET_KEY_____".getBytes(StandardCharsets.UTF_8);
            this.key = Keys.hmacShaKeyFor(fallback);
        } else {
            byte[] keyBytes = Decoders.BASE64.decode(secretB64);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(Map.of(
                        "roles", principal.getRoles(),
                        "uid", principal.getUserId(),
                        "cid", principal.getClientId() == null ? -1 : principal.getClientId()
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }
}
