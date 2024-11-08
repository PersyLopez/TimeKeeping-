import java.util.Scanner;

public class EmployeeInfo {
    private final String employeeName;
    private final String employeeLastName;

    public EmployeeInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter employee's first name:");
        this.employeeName = scanner.nextLine();
        System.out.println("Enter employee's last name:");
        this.employeeLastName = scanner.nextLine();
    }
} 