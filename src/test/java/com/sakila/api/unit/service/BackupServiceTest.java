package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.domain.port.out.BackupStore;
import com.sakila.api.domain.port.out.BackupStore.BackupResult;
import com.sakila.api.domain.service.BackupService;

@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

    @Mock
    private BackupStore backupStore;

    @InjectMocks
    private BackupService backupService;

    @Test
    void createBackupDelegates() {
        BackupResult expected = new BackupResult("backup-20260805-153000.sql", 4096L, Instant.parse("2026-08-05T15:30:00Z"));
        when(backupStore.createBackup()).thenReturn(expected);

        BackupResult result = backupService.createBackup();

        assertSame(expected, result);
    }
}
