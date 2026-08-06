package com.sakila.api.adapter.in.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.BackupResponse;
import com.sakila.api.domain.port.in.BackupUseCase;

@RestController
@RequestMapping("/api/backups")
public class BackupController {

    private final BackupUseCase backupUseCase;

    public BackupController(BackupUseCase backupUseCase) {
        this.backupUseCase = backupUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BackupResponse createBackup() {
        var result = backupUseCase.createBackup();
        return new BackupResponse(result.filename(), result.sizeBytes(), result.generatedAt());
    }
}
