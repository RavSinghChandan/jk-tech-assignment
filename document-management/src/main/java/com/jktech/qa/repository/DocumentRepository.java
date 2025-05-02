package com.jktech.qa.repository;

import com.jktech.qa.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    @Query("SELECT d FROM Document d WHERE " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.keywords) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Document> searchDocuments(@Param("query") String query, Pageable pageable);

    Page<Document> findByAuthor(String author, Pageable pageable);
    
    Page<Document> findByDocumentType(String documentType, Pageable pageable);
    
    Page<Document> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
} 