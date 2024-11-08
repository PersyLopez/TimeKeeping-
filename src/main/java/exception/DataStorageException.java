public class DataStorageException extends RuntimeException {
    private final ErrorCode errorCode;

    public DataStorageException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataStorageException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
} 