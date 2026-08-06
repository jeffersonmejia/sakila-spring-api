package com.sakila.api.domain.service;

import org.springframework.stereotype.Service;

import com.sakila.api.domain.port.in.BackupUseCase;
import com.sakila.api.domain.port.out.BackupStore;

@Service
public class BackupService implements BackupUseCase {

    private final BackupStore backupStore;

    public BackupService(BackupStore backupStore) {
        this.backupStore = backupStore;
    }

    @Override
    public BackupStore.BackupResult createBackup() {
        return backupStore.createBackup();
    }
}
