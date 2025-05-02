package com.jktech.qa.controller;

import com.jktech.qa.model.Document;
import com.jktech.qa.service.QAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QAControllerTest {

    @Mock
    private QAService qaService;

    @InjectMocks
    private QAController qaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchDocuments() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");

        Map<String, Object> mockResponse = Map.of(
                "documents", Arrays.asList(doc),
                "totalElements", 1L,
                "totalPages", 1,
                "currentPage", 0
        );

        when(qaService.searchDocuments(any(), any())).thenReturn(mockResponse);

        // When
        ResponseEntity<Map<String, Object>> response = qaController.searchDocuments(
                "test", 0, 10, "createdAt", "DESC");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.get("totalElements"));
    }

    @Test
    public void testFilterDocuments() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");

        Map<String, Object> mockResponse = Map.of(
                "documents", Arrays.asList(doc),
                "totalElements", 1L,
                "totalPages", 1,
                "currentPage", 0
        );

        when(qaService.filterDocuments(any(), any(), any(), any(), any())).thenReturn(mockResponse);

        // When
        ResponseEntity<Map<String, Object>> response = qaController.filterDocuments(
                "John Doe", "TECHNICAL", "2024-01-01T00:00:00", "2024-12-31T23:59:59",
                0, 10, "createdAt", "DESC");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.get("totalElements"));
    }
} 