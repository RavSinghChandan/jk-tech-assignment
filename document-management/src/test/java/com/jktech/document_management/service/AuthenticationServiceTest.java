package com.jktech.document_management.service;

import com.jktech.document_management.dto.AuthenticationRequest;
import com.jktech.document_management.dto.AuthenticationResponse;
import com.jktech.document_management.dto.RegisterRequest;
import com.jktech.document_management.entity.User;
import com.jktech.document_management.repository.UserRepository;
import com.jktech.document_management.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        mockToken = "test-token";
    }

    @Test
    void register_ShouldCreateUserAndReturnToken() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(mockUser);
        when(jwtService.generateToken(any())).thenReturn(mockToken);

        // When
        AuthenticationResponse response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        verify(userRepository).save(any());
        verify(jwtService).generateToken(any());
    }

    @Test
    void authenticate_ShouldReturnToken() {
        // Given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn(mockToken);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(any());
    }
} 