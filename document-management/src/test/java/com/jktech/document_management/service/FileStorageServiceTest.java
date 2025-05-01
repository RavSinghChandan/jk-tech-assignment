package com.jktech.document_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private Path tempDirectory;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempDirectory = tempDir;
        fileStorageService = new FileStorageService(tempDirectory.toString());
    }

    @Test
    void storeFile_ShouldStoreFileSuccessfully() throws IOException {
        // Given
        String fileName = "test.txt";
        byte[] content = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", content);

        // When
        String storedFileName = fileStorageService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertTrue(Files.exists(tempDirectory.resolve(storedFileName)));
        assertArrayEquals(content, Files.readAllBytes(tempDirectory.resolve(storedFileName)));
    }

    @Test
    void storeFile_ShouldHandleEmptyFile() throws IOException {
        // Given
        String fileName = "empty.txt";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", new byte[0]);

        // When
        String storedFileName = fileStorageService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertTrue(Files.exists(tempDirectory.resolve(storedFileName)));
        assertEquals(0, Files.size(tempDirectory.resolve(storedFileName)));
    }

    @Test
    void storeFile_ShouldHandleLargeFile() throws IOException {
        // Given
        String fileName = "large.txt";
        byte[] content = new byte[1024 * 1024]; // 1MB
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", content);

        // When
        String storedFileName = fileStorageService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertTrue(Files.exists(tempDirectory.resolve(storedFileName)));
        assertEquals(content.length, Files.size(tempDirectory.resolve(storedFileName)));
    }

    @Test
    void storeFile_ShouldHandleSpecialCharactersInFileName() throws IOException {
        // Given
        String fileName = "test@file#name.txt";
        byte[] content = "Test content".getBytes();
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", content);

        // When
        String storedFileName = fileStorageService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertTrue(Files.exists(tempDirectory.resolve(storedFileName)));
        assertArrayEquals(content, Files.readAllBytes(tempDirectory.resolve(storedFileName)));
    }

    @Test
    void storeFile_ShouldThrowExceptionForInvalidFile() {
        // Given
        MultipartFile file = new MockMultipartFile("test.txt", new byte[0]);

        // When/Then
        assertThrows(IOException.class, () -> fileStorageService.storeFile(file));
    }
} 