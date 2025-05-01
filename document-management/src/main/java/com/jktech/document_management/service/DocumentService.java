package com.jktech.document_management.service;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import com.jktech.document_management.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Document uploadDocument(MultipartFile file, String title, User user) throws IOException {
        String filePath = fileStorageService.storeFile(file);
        String content = fileStorageService.extractContent(file);
        
        Document document = Document.builder()
                .title(title)
                .content(content)
                .fileType(file.getContentType())
                .filePath(filePath)
                .uploadedBy(user)
                .uploadedAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        
        return documentRepository.save(document);
    }

    public Page<Document> getUserDocuments(User user, Pageable pageable) {
        return documentRepository.findByUploadedByAndIsDeletedFalse(user, pageable);
    }

    public Page<Document> searchDocuments(String keyword, Pageable pageable) {
        return documentRepository.searchByKeyword(keyword, pageable);
    }

    @Transactional
    public void deleteDocument(Long documentId, User user) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if (!document.getUploadedBy().equals(user)) {
            throw new RuntimeException("Unauthorized to delete this document");
        }
        
        document.setDeleted(true);
        documentRepository.save(document);
    }

    public List<Document> getAllUserDocuments(User user) {
        return documentRepository.findByUploadedByAndIsDeletedFalse(user);
    }
} 