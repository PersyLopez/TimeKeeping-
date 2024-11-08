import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class IntakeTracker {
    private final IntakeTimer timer;
    private final IntakeLogger logger;
    private final CategoryManager categoryManager;
    private final String employeeName;
    private final String employeeLastName;
    private final Scanner scanner;
    private final CategoryHandlers categoryHandlers;

    public IntakeTracker() {
        this.scanner = new Scanner(System.in);
        System.out.println("Enter employee's first name:");
        this.employeeName = scanner.nextLine();
        System.out.println("Enter employee's last name:");
        this.employeeLastName = scanner.nextLine();
        
        this.timer = new IntakeTimer();
        this.logger = new IntakeLogger();
        this.categoryManager = new CategoryManager();
        this.categoryHandlers = new CategoryHandlers();
    }

    public void newIntake() {
        timer.startTimer();
        
        // Basic information collection
        IntakeData basicInfo = collectBasicInfo();
        String category = selectCategory();
        Map<String, String> categoryData;
        
        // Handle specific category data
        switch(category) {
            case "RNS" -> categoryData = categoryHandlers.handleRNS();
            case "NCR" -> categoryData = categoryHandlers.handleNCR();
            case "SCO" -> categoryData = categoryHandlers.handleSCO();
            case "Project" -> categoryData = categoryHandlers.handleProject();
            case "Service" -> categoryData = categoryHandlers.handleService();
            default -> categoryData = new HashMap<>();
        }
        
        timer.endTimer();
        String timerDetails = timer.getTimerDetails();
        System.out.println(timerDetails);

        // Update category durations
        categoryManager.addDuration(category, timer.getDuration());

        // Log the intake
        logger.logIntake(category, timerDetails, basicInfo.name(), basicInfo.lastName(),
                        basicInfo.company(), basicInfo.phoneNumber(), basicInfo.email(),
                        categoryData);
        
        System.out.println("Intake completed. Category: " + category);
    }

    private IntakeData collectBasicInfo() {
        return new IntakeData(
            promptInput("Enter name:"),
            promptInput("Enter last name:"),
            promptInput("Enter company:"),
            promptInput("Enter phone number:"),
            promptInput("Enter email:"),
            null,  // category will be set later
            null   // category data will be set later
        );
    }

    private String promptInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private String selectCategory() {
        System.out.println("Select intake category:");
        System.out.println("1. RNS");
        System.out.println("2. NCR");
        System.out.println("3. SCO");
        System.out.println("4. Project");
        System.out.println("5. Service");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        switch(choice) {
            case 1 -> {
                System.out.println("RNS selected");
                return "RNS";
            }
            case 2 -> {
                System.out.println("NCR selected");
                return "NCR";
            }
            case 3 -> {
                System.out.println("SCO selected");
                return "SCO";
            }
            case 4 -> {
                System.out.println("Project selected");
                return "Project";
            }
            case 5 -> {
                System.out.println("Service selected");
                return "Service";
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                return selectCategory();
            }
        }
    }

    public void endShiftSummary() {
        String summary = categoryManager.generateSummary();
        logger.logSummary(summary);
        System.out.println(summary);
    }

    public void printIntakeLog() {
        logger.printLog();
    }

    public static void main(String[] args) {
        IntakeTracker tracker = new IntakeTracker();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Type 'new' to start a new intake, 'log' to view the intake log, 'summary' for end of shift summary, or 'exit' to quit:");
            String input = scanner.nextLine().toLowerCase();
            
            switch(input) {
                case "new" -> tracker.newIntake();
                case "log" -> tracker.printIntakeLog();
                case "summary" -> tracker.endShiftSummary();
                case "exit" -> {
                    tracker.endShiftSummary();
                    return;
                }
            }
        }
    }
} 