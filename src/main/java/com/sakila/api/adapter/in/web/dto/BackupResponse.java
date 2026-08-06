package com.sakila.api.adapter.in.web.dto;

import java.time.Instant;

public record BackupResponse(String filename, long sizeBytes, Instant generatedAt) {
}
