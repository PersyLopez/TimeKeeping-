import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntakeLogger {
    private final List<String> intakeLog;

    public IntakeLogger() {
        this.intakeLog = new ArrayList<>();
    }

    public void logIntake(String category, String timerDetails, String name, 
                         String lastName, String company, String phoneNumber, 
                         String email, Map<String, String> categoryData) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append(timerDetails).append("\n");
        logEntry.append("Category: ").append(category).append("\n");
        logEntry.append("Name: ").append(name).append("\n");
        logEntry.append("Last Name: ").append(lastName).append("\n");
        logEntry.append("Company: ").append(company).append("\n");
        logEntry.append("Phone: ").append(phoneNumber).append("\n");
        logEntry.append("Email: ").append(email).append("\n");

        // Add category-specific data
        categoryData.forEach((key, value) -> 
            logEntry.append(key).append(": ").append(value).append("\n")
        );

        intakeLog.add(logEntry.toString());
    }

    public void logSummary(String summary) {
        intakeLog.add(summary);
    }

    public void printLog() {
        System.out.println("Intake Log:");
        for (String entry : intakeLog) {
            System.out.println(entry);
            System.out.println("-".repeat(50)); // Separator line
        }
    }
} 