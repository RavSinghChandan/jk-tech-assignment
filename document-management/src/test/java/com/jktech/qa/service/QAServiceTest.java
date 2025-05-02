package com.jktech.qa.service;

import com.jktech.qa.model.Document;
import com.jktech.qa.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QAServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private QAService qaService;

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

        Page<Document> page = new PageImpl<>(Arrays.asList(doc));
        when(documentRepository.searchDocuments(any(), any())).thenReturn(page);

        // When
        Map<String, Object> result = qaService.searchDocuments(
                "test", PageRequest.of(0, 10, Sort.by("createdAt").descending()));

        // Then
        assertNotNull(result);
        assertEquals(1, result.get("totalElements"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(0, result.get("currentPage"));
    }

    @Test
    public void testFilterDocumentsByAuthor() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");

        Page<Document> page = new PageImpl<>(Arrays.asList(doc));
        when(documentRepository.findByAuthor(any(), any())).thenReturn(page);

        // When
        Map<String, Object> result = qaService.filterDocuments(
                "John Doe", null, null, null, PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(1, result.get("totalElements"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(0, result.get("currentPage"));
    }

    @Test
    public void testFilterDocumentsByDocumentType() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");

        Page<Document> page = new PageImpl<>(Arrays.asList(doc));
        when(documentRepository.findByDocumentType(any(), any())).thenReturn(page);

        // When
        Map<String, Object> result = qaService.filterDocuments(
                null, "TECHNICAL", null, null, PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(1, result.get("totalElements"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(0, result.get("currentPage"));
    }
} 