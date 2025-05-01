package com.jktech.document_management.repository;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByUploadedByAndIsDeletedFalse(User user, Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE d.isDeleted = false AND " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Document> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Document> findByUploadedByAndIsDeletedFalse(User user);
} 