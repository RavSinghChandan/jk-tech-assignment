-- Insert performance test users
INSERT INTO users (email, password, role) VALUES
('perf-admin@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'ADMIN'),
('perf-editor@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'EDITOR'),
('perf-viewer@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'VIEWER');

-- Insert performance test documents
DO $$
DECLARE
    i INTEGER;
    doc_type TEXT;
    doc_size BIGINT;
    user_id INTEGER;
BEGIN
    FOR i IN 1..10000 LOOP
        doc_type := CASE (i % 5)
            WHEN 0 THEN 'pdf'
            WHEN 1 THEN 'docx'
            WHEN 2 THEN 'txt'
            WHEN 3 THEN 'xlsx'
            ELSE 'pptx'
        END;
        
        doc_size := (i % 10 + 1) * 1024 * 1024; -- 1MB to 10MB
        user_id := (i % 3) + 1; -- Cycle through the three test users
        
        INSERT INTO documents (title, content, file_type, file_size, uploaded_by_id, uploaded_at)
        VALUES (
            'Performance Test Document ' || i,
            'This is the content of performance test document ' || i || '. ' ||
            'It contains some sample text for testing full-text search capabilities. ' ||
            'The document is part of a performance test suite.',
            doc_type,
            doc_size,
            user_id,
            CURRENT_TIMESTAMP
        );
    END LOOP;
END $$; 