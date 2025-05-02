package com.jktech.qa.service;

import com.jktech.qa.model.Document;
import com.jktech.qa.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class QAService {

    private final DocumentRepository documentRepository;

    @Autowired
    public QAService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Map<String, Object> searchDocuments(String query, Pageable pageable) {
        Page<Document> documents = documentRepository.searchDocuments(query, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("documents", documents.getContent());
        response.put("totalElements", documents.getTotalElements());
        response.put("totalPages", documents.getTotalPages());
        response.put("currentPage", documents.getNumber());
        
        return response;
    }

    public Map<String, Object> filterDocuments(String author, String documentType, 
                                             LocalDateTime startDate, LocalDateTime endDate,
                                             Pageable pageable) {
        Page<Document> documents;
        
        if (author != null) {
            documents = documentRepository.findByAuthor(author, pageable);
        } else if (documentType != null) {
            documents = documentRepository.findByDocumentType(documentType, pageable);
        } else if (startDate != null && endDate != null) {
            documents = documentRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        } else {
            documents = documentRepository.findAll(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("documents", documents.getContent());
        response.put("totalElements", documents.getTotalElements());
        response.put("totalPages", documents.getTotalPages());
        response.put("currentPage", documents.getNumber());
        
        return response;
    }
} 