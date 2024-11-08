public record ValidationResult(boolean isValid, String message) {
    public static ValidationResult valid() {
        return new ValidationResult(true, "");
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }
} 