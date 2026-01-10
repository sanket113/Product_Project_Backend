package com.example.rbac.security;

import com.example.rbac.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for handling JWT token operations.
 * Uses EMAIL as the JWT subject.
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expiration;

    public JwtService(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(properties.getSecret()));
        this.expiration = properties.getExpiration();
    }

    // ================= TOKEN GENERATION =================

    /**
     * Generates a JWT token for the authenticated user.
     * Subject = email
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return createToken(extraClaims, userDetails.getUsername()); // EMAIL
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // EMAIL stored as sub
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= TOKEN EXTRACTION =================

    /**
     * Extracts email from JWT subject.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ================= TOKEN VALIDATION =================

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
