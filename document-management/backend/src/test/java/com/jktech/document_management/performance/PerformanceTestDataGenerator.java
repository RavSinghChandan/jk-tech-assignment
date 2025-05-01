package com.jktech.document_management.performance;

import com.jktech.document_management.entity.Document;
import com.jktech.document_management.entity.User;
import com.jktech.document_management.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerformanceTestDataGenerator {

    private static final Random random = new Random();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String[] DOCUMENT_TYPES = {"pdf", "docx", "txt", "xlsx", "pptx"};
    private static final String[] WORDS = {
        "document", "management", "system", "test", "data", "performance",
        "search", "query", "index", "database", "storage", "retrieval",
        "analysis", "processing", "optimization", "scalability", "reliability"
    };

    public static List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateUser())
                .collect(Collectors.toList());
    }

    public static User generateUser() {
        return User.builder()
                .email(UUID.randomUUID().toString() + "@example.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.values()[random.nextInt(Role.values().length)])
                .build();
    }

    public static List<Document> generateDocuments(User user, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateDocument(user))
                .collect(Collectors.toList());
    }

    public static Document generateDocument(User user) {
        return Document.builder()
                .title(generateRandomTitle())
                .content(generateRandomContent())
                .fileType(DOCUMENT_TYPES[random.nextInt(DOCUMENT_TYPES.length)])
                .fileSize(random.nextLong(1000, 10000000))
                .uploadedBy(user)
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    private static String generateRandomTitle() {
        int wordCount = random.nextInt(3) + 2; // 2-4 words
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            title.append(WORDS[random.nextInt(WORDS.length)]).append(" ");
        }
        return title.toString().trim();
    }

    private static String generateRandomContent() {
        int paragraphCount = random.nextInt(3) + 1; // 1-3 paragraphs
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < paragraphCount; i++) {
            int sentenceCount = random.nextInt(3) + 2; // 2-4 sentences per paragraph
            for (int j = 0; j < sentenceCount; j++) {
                int wordCount = random.nextInt(10) + 5; // 5-14 words per sentence
                for (int k = 0; k < wordCount; k++) {
                    content.append(WORDS[random.nextInt(WORDS.length)]).append(" ");
                }
                content.append(". ");
            }
            content.append("\n\n");
        }
        return content.toString().trim();
    }

    public static String generateSearchTerm() {
        return WORDS[random.nextInt(WORDS.length)];
    }
} 