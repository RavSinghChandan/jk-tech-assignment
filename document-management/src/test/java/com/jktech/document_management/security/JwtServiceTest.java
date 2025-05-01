package com.jktech.document_management.security;

import com.jktech.document_management.model.User;
import com.jktech.document_management.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;
    private String testToken;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        jwtService.setSecretKey(SECRET_KEY);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.USER);

        testToken = jwtService.generateToken(testUser);
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void extractUsername_ShouldExtractEmailFromToken() {
        // When
        String username = jwtService.extractUsername(testToken);

        // Then
        assertEquals(testUser.getEmail(), username);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        // When
        boolean isValid = jwtService.isTokenValid(testToken, testUser);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidUsername() {
        // Given
        User differentUser = new User();
        differentUser.setEmail("different@example.com");

        // When
        boolean isValid = jwtService.isTokenValid(testToken, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalseForExpiredToken() {
        // Given
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", testUser.getEmail());
        claims.put("role", testUser.getRole());

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // 24 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // When
        boolean isValid = jwtService.isTokenValid(expiredToken, testUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void extractClaim_ShouldExtractClaimFromToken() {
        // When
        String role = jwtService.extractClaim(testToken, claims -> claims.get("role", String.class));

        // Then
        assertEquals(testUser.getRole().name(), role);
    }

    @Test
    void generateToken_ShouldIncludeCustomClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");

        // When
        String token = jwtService.generateToken(extraClaims, testUser);

        // Then
        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("customValue", claims.get("customClaim", String.class));
    }
} 