import java.util.*;

public class CategoryHandlers {
    private final Scanner scanner;

    public CategoryHandlers() {
        this.scanner = new Scanner(System.in);
    }

    public Map<String, String> handleNCR() {
        Map<String, String> data = new HashMap<>();
        
        data.put("WJID", promptInput("Enter WJID:"));
        data.put("WM INC", promptInput("Enter WM INC:"));
        data.put("Location", promptInput("Enter location:"));
        data.put("Scope", promptInput("Enter scope:"));
        data.put("Troubleshooting Steps", promptInput("Enter troubleshooting steps taken:"));
        data.put("Tech Needs", promptInput("Enter tech needs:"));
        data.put("Queue Status", promptInput("Is the intake posted in the NCR intake queue? (yes/no)"));
        data.put("Excel Status", promptInput("Is it in the Excel sheet? (yes/no)"));
        data.put("Chat Status", promptInput("Is it in the nightcrawlers chat? (yes/no)"));
        
        return data;
    }

    public Map<String, String> handleProject() {
        Map<String, String> data = new HashMap<>();
        
        data.put("WJID", promptInput("Enter WJID:"));
        data.put("WTS WJID", promptInput("Enter WTS WJID:"));
        data.put("Location", promptInput("Enter location:"));
        data.put("Scope", promptInput("Enter scope:"));
        data.put("Troubleshooting Steps", promptInput("Enter troubleshooting steps taken:"));
        data.put("Tech Needs", promptInput("Enter tech needs:"));
        data.put("Project Notes Status", promptInput("Is the intake posted in the project notes on trust? (yes/no)"));
        data.put("Nightcrawlers Status", promptInput("Is the intake posted on nightcrawlers? (yes/no)"));
        
        return data;
    }

    public Map<String, String> handleService() {
        Map<String, String> data = new HashMap<>();
        
        data.put("Sub-category", promptInput("Enter sub-category:"));
        data.put("WJID", promptInput("Enter WJID:"));
        data.put("INC", promptInput("Enter INC:"));
        data.put("Location", promptInput("Enter location:"));
        data.put("Scope", promptInput("Enter scope:"));
        data.put("Troubleshooting Steps", promptInput("Enter troubleshooting steps taken (optional, press Enter to skip):"));
        data.put("Tech Needs", promptInput("Enter tech needs:"));
        data.put("INC Status", promptInput("Has the INC been updated with the intake notes? (yes/no)"));
        data.put("Nightcrawlers Status", promptInput("Has it been posted on nightcrawlers? (yes/no)"));
        
        return data;
    }

    public Map<String, String> handleSCO() {
        Map<String, String> data = new HashMap<>();
        
        data.put("WJID", promptInput("Enter WJID:"));
        data.put("INC", promptInput("Enter INC:"));
        data.put("Location", promptInput("Enter location:"));
        data.put("Scope", promptInput("Enter scope:"));
        data.put("Troubleshooting Steps", promptInput("Enter troubleshooting steps taken (optional, press Enter to skip):"));
        data.put("Tech Needs", promptInput("Enter tech needs:"));
        data.put("INC Status", promptInput("Has the INC been updated with the intake notes? (yes/no)"));
        data.put("Nightcrawlers Status", promptInput("Has it been posted on nightcrawlers? (yes/no)"));
        
        return data;
    }

    private String promptInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }
} 