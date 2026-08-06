package com.sakila.api.adapter.out.backup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sakila.api.common.exception.InternalException;
import com.sakila.api.domain.port.out.BackupStore;

@Repository
public class PgDumpBackupAdapter implements BackupStore {

    private static final int MAX_BACKUPS = 5;
    private static final DateTimeFormatter FILENAME = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
            .withZone(ZoneId.systemDefault());

    private final Path directory;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public PgDumpBackupAdapter(
            @Value("${app.backup.directory}") String directory,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        this.directory = Path.of(directory).toAbsolutePath().normalize();
        this.username = username;
        this.password = password;
        DbUrl db = DbUrl.parse(url);
        this.host = db.host();
        this.port = db.port();
        this.database = db.database();
    }

    @Override
    public BackupResult createBackup() {
        try {
            Files.createDirectories(directory);
            Instant now = Instant.now();
            Path file = directory.resolve("backup-" + FILENAME.format(now) + ".sql");

            ProcessBuilder pb = new ProcessBuilder(
                    "pg_dump",
                    "--host", host,
                    "--port", String.valueOf(port),
                    "--username", username,
                    "--format", "plain",
                    "--file", file.toString(),
                    database);
            pb.environment().put("PGPASSWORD", password);
            Process process = pb.start();
            int exit = process.waitFor();
            if (exit != 0) {
                Files.deleteIfExists(file);
                throw new InternalException("No se pudo generar el backup de la base de datos");
            }

            rotate();
            long size = Files.size(file);
            return new BackupResult(file.getFileName().toString(), size, now);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InternalException("No se pudo generar el backup de la base de datos");
        }
    }

    private void rotate() throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            List<Path> backups = files
                    .filter(p -> p.getFileName().toString().startsWith("backup-"))
                    .filter(p -> p.getFileName().toString().endsWith(".sql"))
                    .sorted(Comparator.reverseOrder())
                    .toList();
            for (int i = MAX_BACKUPS; i < backups.size(); i++) {
                Files.deleteIfExists(backups.get(i));
            }
        }
    }

    private record DbUrl(String host, int port, String database) {

        private static DbUrl parse(String url) {
            String rest = url.replace("jdbc:postgresql://", "");
            int slash = rest.indexOf('/');
            String hostPort = slash >= 0 ? rest.substring(0, slash) : rest;
            String database = slash >= 0 ? rest.substring(slash + 1) : "";
            int colon = hostPort.lastIndexOf(':');
            if (colon >= 0) {
                return new DbUrl(hostPort.substring(0, colon), Integer.parseInt(hostPort.substring(colon + 1)),
                        database);
            }
            return new DbUrl(hostPort, 5432, database);
        }
    }
}
