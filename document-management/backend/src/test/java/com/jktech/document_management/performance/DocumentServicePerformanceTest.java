package com.jktech.document_management.performance;

import com.jktech.document_management.entity.Document;
import com.jktech.document_management.entity.User;
import com.jktech.document_management.repository.DocumentRepository;
import com.jktech.document_management.service.DocumentService;
import com.jktech.document_management.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DocumentServicePerformanceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    private User testUser;
    private static final int DOCUMENT_COUNT = 1000;
    private static final int SEARCH_ITERATIONS = 100;
    private static final long MAX_SEARCH_TIME_MS = 100;

    @BeforeEach
    void setUp() {
        testUser = TestDataGenerator.generateUser();
        List<Document> documents = TestDataGenerator.generateDocuments(testUser, DOCUMENT_COUNT);
        documentRepository.saveAll(documents);
    }

    @Test
    void searchDocuments_PerformanceTest() {
        long totalTime = 0;
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            String searchTerm = TestDataGenerator.generateRandomText(2);
            long startTime = System.nanoTime();
            
            Page<Document> results = documentService.searchDocuments(
                searchTerm,
                PageRequest.of(0, 10)
            );
            
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            totalTime += duration;
            
            assertTrue(duration < MAX_SEARCH_TIME_MS,
                "Search operation took too long: " + duration + "ms");
        }
        
        double averageTime = (double) totalTime / SEARCH_ITERATIONS;
        System.out.println("Average search time: " + averageTime + "ms");
        assertTrue(averageTime < MAX_SEARCH_TIME_MS,
            "Average search time exceeded maximum allowed time");
    }

    @Test
    void getDocuments_PerformanceTest() {
        long totalTime = 0;
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            
            Page<Document> results = documentService.getDocuments(
                PageRequest.of(i % 10, 10)
            );
            
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            totalTime += duration;
            
            assertTrue(duration < MAX_SEARCH_TIME_MS,
                "Get documents operation took too long: " + duration + "ms");
        }
        
        double averageTime = (double) totalTime / SEARCH_ITERATIONS;
        System.out.println("Average get documents time: " + averageTime + "ms");
        assertTrue(averageTime < MAX_SEARCH_TIME_MS,
            "Average get documents time exceeded maximum allowed time");
    }
} 