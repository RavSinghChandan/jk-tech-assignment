package com.jktech.document_management.controller;

import com.jktech.document_management.dto.AuthenticationRequest;
import com.jktech.document_management.dto.AuthenticationResponse;
import com.jktech.document_management.dto.RegisterRequest;
import com.jktech.document_management.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private AuthenticationResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = AuthenticationResponse.builder()
                .token("test-token")
                .build();
    }

    @Test
    void register_ShouldReturnAuthenticationResponse() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = authenticationController.register(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse() {
        // Given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(mockResponse);

        // When
        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }
} 