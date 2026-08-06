package com.sakila.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;

class BackupIntegrationTest extends AbstractIntegrationTest {

    @Test
    void adminCanCreateBackupAndRetentionKeepsFiveFiles() throws Exception {
        Path realFile = createBackup();

        org.junit.jupiter.api.Assertions.assertTrue(Files.exists(realFile));
        org.junit.jupiter.api.Assertions.assertTrue(Files.size(realFile) > 0);
        org.junit.jupiter.api.Assertions.assertEquals(5, countBackupFiles());
    }

    @Test
    void employeeCannotCreateBackup() throws Exception {
        mockMvc.perform(post("/api/backups").header("Authorization", "Bearer " + employeeToken()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    private Path createBackup() throws Exception {
        seedOldDummyBackups(5);

        String body = mockMvc.perform(post("/api/backups").header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename").exists())
                .andExpect(jsonPath("$.sizeBytes").isNumber())
                .andExpect(jsonPath("$.generatedAt").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(body);
        String filename = json.get("filename").asText();
        org.junit.jupiter.api.Assertions.assertTrue(filename.matches("backup-\\d{8}-\\d{6}\\.sql"));
        return BACKUP_DIR.resolve(filename);
    }

    private void seedOldDummyBackups(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            Path file = BACKUP_DIR.resolve(String.format("backup-2000010%d-000000.sql", i + 1));
            Files.writeString(file, "-- dummy backup");
        }
    }

    private long countBackupFiles() throws IOException {
        try (var stream = Files.list(BACKUP_DIR)) {
            return stream.filter(p -> {
                String name = p.getFileName().toString();
                return name.startsWith("backup-") && name.endsWith(".sql");
            }).count();
        }
    }
}
