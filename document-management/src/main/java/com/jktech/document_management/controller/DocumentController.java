package com.jktech.document_management.controller;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import com.jktech.document_management.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
\
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return ResponseEntity.ok(documentService.uploadDocument(file, title, user));
    }

    @GetMapping
    public ResponseEntity<Page<Document>> getUserDocuments(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.getUserDocuments(user, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Document>> searchDocuments(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.searchDocuments(keyword, pageable));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal User user
    ) {
        documentService.deleteDocument(documentId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllUserDocuments(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(documentService.getAllUserDocuments(user));
    }
} 