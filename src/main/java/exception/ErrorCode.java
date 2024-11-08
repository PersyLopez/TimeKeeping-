public enum ErrorCode {
    BACKUP_ERROR("BACKUP-001"),
    RESTORE_ERROR("BACKUP-002"),
    INTEGRITY_ERROR("BACKUP-003");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
} 