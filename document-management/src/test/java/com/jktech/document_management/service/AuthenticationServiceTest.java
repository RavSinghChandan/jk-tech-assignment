package com.jktech.document_management.service;

import com.jktech.document_management.dto.AuthenticationRequest;
import com.jktech.document_management.dto.AuthenticationResponse;
import com.jktech.document_management.dto.RegisterRequest;
import com.jktech.document_management.model.User;
import com.jktech.document_management.model.Role;
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
    private AuthenticationRequest authenticationRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole(Role.USER);

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.USER);
    }

    @Test
    void register_ShouldCreateUserAndReturnToken() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());

        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_ShouldThrowExceptionWhenEmailExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When/Then
        assertThrows(RuntimeException.class, () -> authenticationService.register(registerRequest));
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_ShouldReturnToken() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(testUser, null));

        // When
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());

        verify(userRepository).findByEmail(authenticationRequest.getEmail());
        verify(jwtService).generateToken(any(User.class));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void authenticate_ShouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(authenticationRequest));
        verify(userRepository).findByEmail(authenticationRequest.getEmail());
        verify(jwtService, never()).generateToken(any(User.class));
    }
} 