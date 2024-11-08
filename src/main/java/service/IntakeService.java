public class IntakeService {
    private final FileStorageManager storageManager;
    private final IntakeLogger logger;
    private final InputValidator validator;

    public IntakeService() {
        this.storageManager = new FileStorageManager(AppConfig.getProperty("data.directory"));
        this.logger = new IntakeLogger();
        this.validator = new InputValidator();
    }

    public String createIntake(IntakeData intake) {
        validateIntake(intake);
        String id = storageManager.saveIntake(intake);
        logger.logIntake(intake);
        return id;
    }

    private void validateIntake(IntakeData intake) {
        // Validation implementation needed
    }
} 