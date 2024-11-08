package backup;

import exception.DataStorageException;
import exception.ErrorCode;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.zip.*;
import java.io.*;
import java.util.*;

public class BackupManager {
    private final Path backupDirectory;
    private final Path dataDirectory;
    private static final DateTimeFormatter BACKUP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final int DEFAULT_RETENTION_DAYS = 30;

    public BackupManager(Path dataDir, Path backupDir) {
        this.dataDirectory = dataDir;
        this.backupDirectory = backupDir;
        initializeBackupDirectory();
    }

    private void initializeBackupDirectory() {
        try {
            Files.createDirectories(backupDirectory);
        } catch (IOException e) {
            throw new DataStorageException("Failed to create backup directory", ErrorCode.BACKUP_ERROR);
        }
    }

    public void createBackup(String description) {
        String timestamp = LocalDateTime.now().format(BACKUP_FORMAT);
        Path backupFile = backupDirectory.resolve("backup_" + timestamp + ".zip");
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backupFile.toFile()))) {
            // Create backup metadata
            createBackupMetadata(zos, description);
            
            // Backup all data files
            Files.walk(dataDirectory)
                .filter(Files::isRegularFile)
                .forEach(file -> addToZip(zos, file));
            
            System.out.println("Backup created successfully: " + backupFile);
        } catch (IOException e) {
            throw new DataStorageException("Failed to create backup: " + e.getMessage(), ErrorCode.BACKUP_ERROR);
        }
    }

    private void createBackupMetadata(ZipOutputStream zos, String description) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("timestamp", LocalDateTime.now().toString());
        metadata.put("description", description);
        metadata.put("version", "1.0");

        ZipEntry metadataEntry = new ZipEntry("metadata.json");
        zos.putNextEntry(metadataEntry);
        zos.write(new ObjectMapper().writeValueAsString(metadata).getBytes());
        zos.closeEntry();
    }

    private void addToZip(ZipOutputStream zos, Path file) {
        try {
            String zipEntryPath = dataDirectory.relativize(file).toString();
            ZipEntry entry = new ZipEntry(zipEntryPath);
            zos.putNextEntry(entry);
            Files.copy(file, zos);
            zos.closeEntry();
        } catch (IOException e) {
            System.err.println("Failed to add file to backup: " + file);
        }
    }

    public void restoreFromBackup(Path backupFile, boolean overwrite) {
        Path tempDir = createTempDirectory();
        
        try {
            // Extract backup to temp directory
            extractBackup(backupFile, tempDir);
            
            // Verify backup integrity
            if (!verifyBackupIntegrity(tempDir)) {
                throw new DataStorageException("Backup integrity check failed", ErrorCode.BACKUP_ERROR);
            }

            // Perform restore
            restoreFiles(tempDir, overwrite);
            
            System.out.println("Restore completed successfully");
        } catch (IOException e) {
            throw new DataStorageException("Failed to restore from backup: " + e.getMessage(), ErrorCode.BACKUP_ERROR);
        } finally {
            cleanupTempDirectory(tempDir);
        }
    }

    private Path createTempDirectory() {
        try {
            return Files.createTempDirectory("intake_restore_");
        } catch (IOException e) {
            throw new DataStorageException("Failed to create temp directory", ErrorCode.BACKUP_ERROR);
        }
    }

    private void extractBackup(Path backupFile, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(backupFile.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path targetPath = targetDir.resolve(entry.getName());
                
                // Create parent directories if needed
                Files.createDirectories(targetPath.getParent());
                
                // Extract file
                Files.copy(zis, targetPath, StandardCopyOption.REPLACE_EXISTING);
                zis.closeEntry();
            }
        }
    }

    private boolean verifyBackupIntegrity(Path extractedDir) {
        try {
            // Check for required directories and files
            boolean hasJsonData = Files.exists(extractedDir.resolve("json_data"));
            boolean hasReadableLogs = Files.exists(extractedDir.resolve("readable_logs"));
            boolean hasMetadata = Files.exists(extractedDir.resolve("metadata.json"));
            
            return hasJsonData && hasReadableLogs && hasMetadata;
        } catch (Exception e) {
            return false;
        }
    }

    private void restoreFiles(Path sourceDir, boolean overwrite) throws IOException {
        Files.walk(sourceDir)
            .filter(Files::isRegularFile)
            .forEach(source -> {
                try {
                    Path relativePath = sourceDir.relativize(source);
                    Path targetPath = dataDirectory.resolve(relativePath);
                    
                    if (overwrite || !Files.exists(targetPath)) {
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to restore file: " + source);
                }
            });
    }

    private void cleanupTempDirectory(Path tempDir) {
        try {
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete temporary file: " + path);
                    }
                });
        } catch (IOException e) {
            System.err.println("Failed to cleanup temporary directory: " + tempDir);
        }
    }

    public void scheduleAutomaticBackups(Duration interval) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    createBackup("Automated backup");
                    cleanupOldBackups();
                } catch (Exception e) {
                    System.err.println("Automated backup failed: " + e.getMessage());
                }
            }
        }, 0, interval.toMillis());
    }

    private void cleanupOldBackups() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(DEFAULT_RETENTION_DAYS);
            
            Files.list(backupDirectory)
                .filter(path -> path.toString().endsWith(".zip"))
                .filter(path -> {
                    try {
                        String filename = path.getFileName().toString();
                        LocalDateTime backupDate = LocalDateTime.parse(
                            filename.substring(7, filename.length() - 4),
                            BACKUP_FORMAT
                        );
                        return backupDate.isBefore(cutoffDate);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete old backup: " + path);
                    }
                });
        } catch (IOException e) {
            System.err.println("Failed to cleanup old backups: " + e.getMessage());
        }
    }
} 