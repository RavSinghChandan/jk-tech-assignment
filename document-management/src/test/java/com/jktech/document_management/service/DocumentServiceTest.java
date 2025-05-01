package com.jktech.document_management.service;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import com.jktech.document_management.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private DocumentService documentService;

    private User testUser;
    private Document testDocument;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        testDocument = Document.builder()
                .id(1L)
                .title("Test Document")
                .content("Test Content")
                .fileType("application/pdf")
                .filePath("/path/to/file")
                .uploadedBy(testUser)
                .uploadedAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        testFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Test content".getBytes()
        );
    }

    @Test
    void uploadDocument_Success() throws IOException {
        when(fileStorageService.storeFile(any())).thenReturn("/path/to/file");
        when(fileStorageService.extractContent(any())).thenReturn("Test content");
        when(documentRepository.save(any())).thenReturn(testDocument);

        Document result = documentService.uploadDocument(testFile, "Test Document", testUser);

        assertNotNull(result);
        assertEquals("Test Document", result.getTitle());
        verify(documentRepository, times(1)).save(any());
    }

    @Test
    void getUserDocuments_Success() {
        Page<Document> page = new PageImpl<>(Collections.singletonList(testDocument));
        when(documentRepository.findByUploadedByAndIsDeletedFalse(any(), any())).thenReturn(page);

        Page<Document> result = documentService.getUserDocuments(testUser, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository, times(1)).findByUploadedByAndIsDeletedFalse(any(), any());
    }

    @Test
    void searchDocuments_Success() {
        Page<Document> page = new PageImpl<>(Collections.singletonList(testDocument));
        when(documentRepository.searchByKeyword(any(), any())).thenReturn(page);

        Page<Document> result = documentService.searchDocuments("test", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository, times(1)).searchByKeyword(any(), any());
    }

    @Test
    void deleteDocument_Success() {
        when(documentRepository.findById(any())).thenReturn(Optional.of(testDocument));
        when(documentRepository.save(any())).thenReturn(testDocument);

        documentService.deleteDocument(1L, testUser);

        assertTrue(testDocument.isDeleted());
        verify(documentRepository, times(1)).save(any());
    }

    @Test
    void getAllUserDocuments_Success() {
        List<Document> documents = Collections.singletonList(testDocument);
        when(documentRepository.findByUploadedByAndIsDeletedFalse(any())).thenReturn(documents);

        List<Document> result = documentService.getAllUserDocuments(testUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(documentRepository, times(1)).findByUploadedByAndIsDeletedFalse(any());
    }
} 