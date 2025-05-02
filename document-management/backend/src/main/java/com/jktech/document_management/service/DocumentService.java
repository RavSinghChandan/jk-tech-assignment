package com.jktech.document_management.service;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import com.jktech.document_management.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public Document uploadDocument(Document document) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        document.setUploadedBy(currentUser);
        return documentRepository.save(document);
    }

    public Page<Document> getDocuments(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return documentRepository.findByUploadedBy(currentUser, pageable);
    }

    public Page<Document> searchDocuments(String query, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return documentRepository.searchByUserAndQuery(currentUser, query, pageable);
    }

    @Transactional
    public void deleteDocument(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if (!document.getUploadedBy().equals(currentUser)) {
            throw new RuntimeException("You are not authorized to delete this document");
        }
        
        documentRepository.delete(document);
    }
} 