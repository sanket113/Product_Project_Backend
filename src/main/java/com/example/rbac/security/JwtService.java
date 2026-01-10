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
 * Provides methods for generating, validating, and extracting information from JWT tokens.
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expiration;

    /**
     * Constructor that initializes the signing key and expiration time from properties.
     * @param properties JWT configuration properties
     */
    public JwtService(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(properties.getSecret()));
        this.expiration = properties.getExpiration();
    }

    // ================= TOKEN GENERATION =================

    /**
     * Generates a JWT token for the given user details.
     * @param userDetails the user details
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername());
    }

    /**
     * Creates a JWT token with claims and subject.
     * @param claims additional claims to include
     * @param subject the subject (username)
     * @return the created token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= TOKEN VALIDATION =================

    /**
     * Extracts the username from the JWT token.
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
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

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
