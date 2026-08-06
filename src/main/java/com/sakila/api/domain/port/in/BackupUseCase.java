package com.sakila.api.domain.port.in;

import com.sakila.api.domain.port.out.BackupStore;

public interface BackupUseCase {

    BackupStore.BackupResult createBackup();
}
