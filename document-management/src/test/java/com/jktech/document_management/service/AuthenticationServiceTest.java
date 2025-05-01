package com.jktech.document_management.service;

import com.jktech.document_management.dto.AuthenticationRequest;
import com.jktech.document_management.dto.AuthenticationResponse;
import com.jktech.document_management.dto.RegisterRequest;
import com.jktech.document_management.model.Role;
import com.jktech.document_management.model.User;
import com.jktech.document_management.repository.UserRepository;
import com.jktech.document_management.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        authRequest = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.VIEWER)
                .build();
    }

    @Test
    void register_Success() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(testUser);
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }
} 