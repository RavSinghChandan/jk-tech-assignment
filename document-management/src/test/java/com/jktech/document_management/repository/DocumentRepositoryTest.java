package com.jktech.document_management.repository;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import com.jktech.document_management.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Document testDocument;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);
        testUser = userRepository.save(testUser);

        testDocument = new Document();
        testDocument.setTitle("Test Document");
        testDocument.setContent("Test Content");
        testDocument.setFileType("pdf");
        testDocument.setFileSize(1024L);
        testDocument.setUploadedBy(testUser);
        testDocument = documentRepository.save(testDocument);
    }

    @Test
    void findByUploadedBy_ShouldReturnDocuments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> documents = documentRepository.findByUploadedBy(testUser, pageable);
        
        assertNotNull(documents);
        assertEquals(1, documents.getTotalElements());
        assertEquals(testDocument.getId(), documents.getContent().get(0).getId());
    }

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnDocuments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> documents = documentRepository.findByTitleContainingIgnoreCase("test", pageable);
        
        assertNotNull(documents);
        assertEquals(1, documents.getTotalElements());
        assertEquals(testDocument.getId(), documents.getContent().get(0).getId());
    }

    @Test
    void findByContentContainingIgnoreCase_ShouldReturnDocuments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> documents = documentRepository.findByContentContainingIgnoreCase("content", pageable);
        
        assertNotNull(documents);
        assertEquals(1, documents.getTotalElements());
        assertEquals(testDocument.getId(), documents.getContent().get(0).getId());
    }

    @Test
    void findByIdAndUploadedBy_ShouldReturnDocument() {
        Optional<Document> document = documentRepository.findByIdAndUploadedBy(testDocument.getId(), testUser);
        
        assertTrue(document.isPresent());
        assertEquals(testDocument.getId(), document.get().getId());
    }

    @Test
    void findByIdAndUploadedBy_ShouldReturnEmptyForInvalidId() {
        Optional<Document> document = documentRepository.findByIdAndUploadedBy(999L, testUser);
        assertFalse(document.isPresent());
    }

    @Test
    void deleteByIdAndUploadedBy_ShouldDeleteDocument() {
        documentRepository.deleteByIdAndUploadedBy(testDocument.getId(), testUser);
        
        Optional<Document> deletedDocument = documentRepository.findById(testDocument.getId());
        assertFalse(deletedDocument.isPresent());
    }
} 