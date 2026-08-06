package com.sakila.api.adapter.in.web.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of generating a database backup")
public record BackupResponse(
        @Schema(description = "Name of the generated backup file. Backups are kept in `.local/backup`, keeping a maximum of 5 files.", example = "backup-20260805-153000.sql")
        String filename,
        @Schema(description = "Size of the backup file in bytes", example = "15728640")
        long sizeBytes,
        @Schema(description = "Date and time the backup was generated", example = "2026-08-05T15:30:00Z")
        Instant generatedAt) {
}
