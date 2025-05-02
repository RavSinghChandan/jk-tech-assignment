package com.jktech.document_management.controller;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> uploadDocument(@RequestBody Document document) {
        return new ResponseEntity<>(documentService.uploadDocument(document), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Document>> getDocuments(Pageable pageable) {
        return ResponseEntity.ok(documentService.getDocuments(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Document>> searchDocuments(
            @RequestParam String query,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.searchDocuments(query, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
} 