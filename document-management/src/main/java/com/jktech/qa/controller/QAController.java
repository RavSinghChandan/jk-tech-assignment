package com.jktech.qa.controller;

import com.jktech.qa.service.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/qa")
public class QAController {

    private final QAService qaService;

    @Autowired
    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        PageRequest pageable = PageRequest.of(page, size, 
                Sort.by(Sort.Direction.fromString(direction), sortBy));
        
        return ResponseEntity.ok(qaService.searchDocuments(query, pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDocuments(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        PageRequest pageable = PageRequest.of(page, size, 
                Sort.by(Sort.Direction.fromString(direction), sortBy));
        
        LocalDateTime startDateTime = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime endDateTime = endDate != null ? LocalDateTime.parse(endDate) : null;
        
        return ResponseEntity.ok(qaService.filterDocuments(
                author, documentType, startDateTime, endDateTime, pageable));
    }
} 