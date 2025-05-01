package com.jktech.document_management.security;

import com.jktech.document_management.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private User mockUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", mockUser.getEmail());
        claims.put("id", mockUser.getId());

        mockToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(jwtService.getSigningKey())
                .compact();
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // When
        String token = jwtService.generateToken(mockUser);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void extractUsername_ShouldReturnEmail() {
        // When
        String email = jwtService.extractUsername(mockToken);

        // Then
        assertEquals(mockUser.getEmail(), email);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        // Given
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(mockUser.getEmail());

        // When
        boolean isValid = jwtService.isTokenValid(mockToken, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidUsername() {
        // Given
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("different@example.com");

        // When
        boolean isValid = jwtService.isTokenValid(mockToken, userDetails);

        // Then
        assertFalse(isValid);
    }

    @Test
    void extractClaim_ShouldReturnCorrectValue() {
        // When
        String subject = jwtService.extractClaim(mockToken, Claims::getSubject);

        // Then
        assertEquals(mockUser.getEmail(), subject);
    }
} 