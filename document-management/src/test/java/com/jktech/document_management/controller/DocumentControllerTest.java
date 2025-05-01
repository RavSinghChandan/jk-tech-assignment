package com.jktech.document_management.controller;

import com.jktech.document_management.dto.DocumentDTO;
import com.jktech.document_management.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private DocumentDTO mockDocument;
    private Page<DocumentDTO> mockPage;

    @BeforeEach
    void setUp() {
        mockDocument = DocumentDTO.builder()
                .id(1L)
                .title("Test Document")
                .fileType("pdf")
                .build();

        List<DocumentDTO> documents = Arrays.asList(mockDocument);
        mockPage = new PageImpl<>(documents);
    }

    @Test
    void uploadDocument_ShouldReturnCreated() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        when(documentService.uploadDocument(any(), any())).thenReturn(mockDocument);

        // When
        ResponseEntity<DocumentDTO> response = documentController.uploadDocument(file, "Test Document");

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockDocument, response.getBody());
        verify(documentService).uploadDocument(any(), any());
    }

    @Test
    void getDocuments_ShouldReturnPageOfDocuments() {
        // Given
        when(documentService.getDocuments(any(), any())).thenReturn(mockPage);

        // When
        ResponseEntity<Page<DocumentDTO>> response = documentController.getDocuments(0, 10);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
        verify(documentService).getDocuments(any(), any());
    }

    @Test
    void searchDocuments_ShouldReturnPageOfDocuments() {
        // Given
        when(documentService.searchDocuments(any(), any(), any())).thenReturn(mockPage);

        // When
        ResponseEntity<Page<DocumentDTO>> response = documentController.searchDocuments("test", 0, 10);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
        verify(documentService).searchDocuments(any(), any(), any());
    }

    @Test
    void deleteDocument_ShouldReturnNoContent() {
        // Given
        doNothing().when(documentService).deleteDocument(any());

        // When
        ResponseEntity<Void> response = documentController.deleteDocument(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(documentService).deleteDocument(any());
    }
} 