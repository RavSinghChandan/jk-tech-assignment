package com.jktech.qa.repository;

import com.jktech.qa.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void testSearchDocuments() {
        // Given
        Document doc1 = new Document();
        doc1.setTitle("Test Document 1");
        doc1.setContent("This is a test document about Java programming");
        doc1.setAuthor("John Doe");
        doc1.setCreatedAt(LocalDateTime.now());
        doc1.setDocumentType("TECHNICAL");
        doc1.setKeywords("java, programming, test");
        documentRepository.save(doc1);

        Document doc2 = new Document();
        doc2.setTitle("Test Document 2");
        doc2.setContent("This is another test document");
        doc2.setAuthor("Jane Smith");
        doc2.setCreatedAt(LocalDateTime.now());
        doc2.setDocumentType("GENERAL");
        doc2.setKeywords("test, general");
        documentRepository.save(doc2);

        // When
        Page<Document> results = documentRepository.searchDocuments(
                "java", PageRequest.of(0, 10, Sort.by("createdAt").descending()));

        // Then
        assertEquals(1, results.getTotalElements());
        assertEquals("Test Document 1", results.getContent().get(0).getTitle());
    }

    @Test
    public void testFindByAuthor() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");
        documentRepository.save(doc);

        // When
        Page<Document> results = documentRepository.findByAuthor(
                "John Doe", PageRequest.of(0, 10));

        // Then
        assertEquals(1, results.getTotalElements());
        assertEquals("John Doe", results.getContent().get(0).getAuthor());
    }

    @Test
    public void testFindByDocumentType() {
        // Given
        Document doc = new Document();
        doc.setTitle("Test Document");
        doc.setContent("Test content");
        doc.setAuthor("John Doe");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setDocumentType("TECHNICAL");
        doc.setKeywords("test");
        documentRepository.save(doc);

        // When
        Page<Document> results = documentRepository.findByDocumentType(
                "TECHNICAL", PageRequest.of(0, 10));

        // Then
        assertEquals(1, results.getTotalElements());
        assertEquals("TECHNICAL", results.getContent().get(0).getDocumentType());
    }
} 