import java.util.Scanner;

public class IntakeInputHandler {
    private final Scanner scanner;

    public IntakeInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public IntakeData collectBasicInfo() {
        return new IntakeData(
            promptString("Enter name:"),
            promptString("Enter last name:"),
            promptString("Enter company:"),
            promptString("Enter phone number:"),
            promptString("Enter email:")
        );
    }

    public String selectCategory() {
        System.out.println("Select intake category:");
        System.out.println("1. RNS\n2. NCR\n3. SCO\n4. Project\n5. Service");
        int choice = Integer.parseInt(scanner.nextLine());
        return switch(choice) {
            case 1 -> "RNS";
            case 2 -> "NCR";
            case 3 -> "SCO";
            case 4 -> "Project";
            case 5 -> "Service";
            default -> "Unknown";
        };
    }

    // Existing handler methods (handleRNS, handleNCR, etc.) would go here
    // but refactored to return data objects instead of void
} 