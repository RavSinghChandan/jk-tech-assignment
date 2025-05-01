package com.jktech.document_management.service;

import com.jktech.document_management.dto.DocumentDTO;
import com.jktech.document_management.entity.Document;
import com.jktech.document_management.entity.User;
import com.jktech.document_management.repository.DocumentRepository;
import com.jktech.document_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private DocumentService documentService;

    private Document mockDocument;
    private User mockUser;
    private Page<Document> mockPage;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        mockDocument = Document.builder()
                .id(1L)
                .title("Test Document")
                .fileType("pdf")
                .filePath("/path/to/file")
                .uploadedBy(mockUser)
                .build();

        List<Document> documents = Arrays.asList(mockDocument);
        mockPage = new PageImpl<>(documents);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));
    }

    @Test
    void uploadDocument_ShouldSaveAndReturnDocument() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        when(documentRepository.save(any())).thenReturn(mockDocument);

        // When
        DocumentDTO result = documentService.uploadDocument(file, "Test Document");

        // Then
        assertNotNull(result);
        assertEquals(mockDocument.getId(), result.getId());
        assertEquals(mockDocument.getTitle(), result.getTitle());
        verify(documentRepository).save(any());
    }

    @Test
    void getDocuments_ShouldReturnPageOfDocuments() {
        // Given
        when(documentRepository.findByUploadedBy(any(), any())).thenReturn(mockPage);

        // When
        Page<DocumentDTO> result = documentService.getDocuments(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository).findByUploadedBy(any(), any());
    }

    @Test
    void searchDocuments_ShouldReturnPageOfDocuments() {
        // Given
        when(documentRepository.searchByTitleAndUser(any(), any(), any())).thenReturn(mockPage);

        // When
        Page<DocumentDTO> result = documentService.searchDocuments("test", 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository).searchByTitleAndUser(any(), any(), any());
    }

    @Test
    void deleteDocument_ShouldDeleteDocument() {
        // Given
        when(documentRepository.findById(any())).thenReturn(Optional.of(mockDocument));
        doNothing().when(documentRepository).delete(any());

        // When
        documentService.deleteDocument(1L);

        // Then
        verify(documentRepository).delete(any());
    }
} 