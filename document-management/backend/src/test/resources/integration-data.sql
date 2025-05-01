-- Insert test users
INSERT INTO users (id, email, password, role, created_at, updated_at)
VALUES 
    (1, 'admin@test.com', '$2a$10$X7v3z8q9r6t5y4u3i2o1p0n9m8l7k6j5h4g3f2e1d0c0b0a9z8y7x6w5v4u3i2o1p', 'ADMIN', NOW(), NOW()),
    (2, 'editor@test.com', '$2a$10$X7v3z8q9r6t5y4u3i2o1p0n9m8l7k6j5h4g3f2e1d0c0b0a9z8y7x6w5v4u3i2o1p', 'EDITOR', NOW(), NOW()),
    (3, 'viewer@test.com', '$2a$10$X7v3z8q9r6t5y4u3i2o1p0n9m8l7k6j5h4g3f2e1d0c0b0a9z8y7x6w5v4u3i2o1p', 'VIEWER', NOW(), NOW());

-- Insert test documents
DO $$
DECLARE
    i INTEGER;
    user_id INTEGER;
    file_types TEXT[] := ARRAY['pdf', 'docx', 'txt', 'xlsx', 'pptx'];
    file_sizes INTEGER[] := ARRAY[1024, 2048, 3072, 4096, 5120];
BEGIN
    FOR i IN 1..100 LOOP
        -- Cycle through users
        user_id := (i % 3) + 1;
        
        INSERT INTO documents (
            id,
            title,
            content,
            file_type,
            file_size,
            uploaded_by_id,
            created_at,
            updated_at
        ) VALUES (
            i,
            'Integration Test Document ' || i,
            'This is the content of integration test document ' || i || '. It contains some sample text for testing purposes.',
            file_types[(i % 5) + 1],
            file_sizes[(i % 5) + 1],
            user_id,
            NOW(),
            NOW()
        );
    END LOOP;
END $$; 