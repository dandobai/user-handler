package com.dandobai.user_handler.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Generate JWT token for the user email
     */
    public String generateToken(String name) {
        return Jwts.builder()
                .setSubject(name)  // Set name as subject
                .claim("name", name)  // Adding name as a custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)  // Use HS256 for signing
                .compact();
    }

    /**
     * Validate JWT token by comparing the username and checking expiration
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extract username (subject) from the JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract email from the JWT token (using a custom claim "email")
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));  // Extracting email claim
    }

    /**
     * Extract expiration date from the JWT token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method to extract claims
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse the JWT and extract all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()  // Using parserBuilder instead of deprecated parser
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
