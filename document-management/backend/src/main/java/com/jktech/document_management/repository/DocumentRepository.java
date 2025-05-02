package com.jktech.document_management.repository;

import com.jktech.document_management.model.Document;
import com.jktech.document_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByUploadedBy(User user, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.uploadedBy = :user AND " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Document> searchByUserAndQuery(
            @Param("user") User user,
            @Param("query") String query,
            Pageable pageable
    );
} 