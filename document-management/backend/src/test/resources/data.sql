-- Insert test users
INSERT INTO users (email, password, role) VALUES
('admin@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'ADMIN'),
('editor@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'EDITOR'),
('viewer@example.com', '$2a$10$X7G3Y5J8K9L0M1N2O3P4Q5R6S7T8U9V0W1X2Y3Z4A5B6C7D8E9F0G1H2', 'VIEWER');

-- Insert test documents
INSERT INTO documents (title, content, file_type, file_size, uploaded_by_id, uploaded_at) VALUES
('Test Document 1', 'This is the content of test document 1', 'pdf', 1024, 1, CURRENT_TIMESTAMP),
('Test Document 2', 'This is the content of test document 2', 'docx', 2048, 2, CURRENT_TIMESTAMP),
('Test Document 3', 'This is the content of test document 3', 'txt', 512, 3, CURRENT_TIMESTAMP),
('Test Document 4', 'This is the content of test document 4', 'pdf', 1536, 1, CURRENT_TIMESTAMP),
('Test Document 5', 'This is the content of test document 5', 'docx', 2560, 2, CURRENT_TIMESTAMP); 