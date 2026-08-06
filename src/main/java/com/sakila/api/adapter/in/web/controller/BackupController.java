package com.sakila.api.adapter.in.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakila.api.adapter.in.web.dto.BackupResponse;
import com.sakila.api.adapter.in.web.dto.ErrorResponse;
import com.sakila.api.domain.port.in.BackupUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/backups")
@Tag(name = "Backups", description = "Database backup generation. Only the ADMIN role can create backups.")
public class BackupController {

    private final BackupUseCase backupUseCase;

    public BackupController(BackupUseCase backupUseCase) {
        this.backupUseCase = backupUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate a database backup",
            description = "Runs pg_dump over the whole database and stores a `.sql` file in the backup directory. "
                    + "Only the 5 most recent backups are kept; older files are deleted automatically. "
                    + "Responds with 403 when the caller is not an ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Backup generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BackupResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated but without the ADMIN role",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "pg_dump failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    public BackupResponse createBackup() {
        var result = backupUseCase.createBackup();
        return new BackupResponse(result.filename(), result.sizeBytes(), result.generatedAt());
    }
}
