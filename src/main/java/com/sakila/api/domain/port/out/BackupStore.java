package com.sakila.api.domain.port.out;

import java.time.Instant;

public interface BackupStore {

    BackupResult createBackup();

    record BackupResult(String filename, long sizeBytes, Instant generatedAt) {
    }
}
