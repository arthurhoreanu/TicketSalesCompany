package presentation;

import controller.Controller;
import java.util.Scanner;

public class AdminMenu {
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Admin Menu ====");
        System.out.println("1. Create Account");
        System.out.println("2. Logout");
        System.out.println("3. Delete User Account");
        System.out.println("0. Exit");
        System.out.println("====================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                MenuActions.handleCreateAccount(scanner, controller);
                break;
            case "2":
                controller.logout();
                System.out.println("Logged out successfully.");
                break;
            case "3":
                MenuActions.handleDeleteUserAccount(scanner, controller);
                break;
            case "0":
                System.out.println("Exiting the application. Goodbye!");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
}
