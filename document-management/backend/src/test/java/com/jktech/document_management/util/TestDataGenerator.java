package com.jktech.document_management.util;

import com.jktech.document_management.entity.Document;
import com.jktech.document_management.entity.User;
import com.jktech.document_management.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataGenerator {

    private static final Random random = new Random();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User generateUser() {
        return User.builder()
                .email(UUID.randomUUID().toString() + "@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.values()[random.nextInt(Role.values().length)])
                .build();
    }

    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateUser());
        }
        return users;
    }

    public static Document generateDocument(User user) {
        return Document.builder()
                .title("Test Document " + UUID.randomUUID().toString())
                .content("This is a test document content.")
                .fileType("pdf")
                .fileSize(random.nextLong(1000, 1000000))
                .uploadedBy(user)
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    public static List<Document> generateDocuments(User user, int count) {
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            documents.add(generateDocument(user));
        }
        return documents;
    }

    public static String generateRandomText(int wordCount) {
        String[] words = {"the", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog",
                "document", "management", "system", "test", "data", "generation"};
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            text.append(words[random.nextInt(words.length)]).append(" ");
        }
        return text.toString().trim();
    }
} 