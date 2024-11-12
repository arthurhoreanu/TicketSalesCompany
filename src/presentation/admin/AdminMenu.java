package presentation.admin;

import controller.Controller;
import presentation.AccountAction;

import java.util.Scanner;

public class AdminMenu {
    public static boolean display(Scanner scanner, Controller controller) {
        System.out.println("==== Admin Menu ====");
        System.out.println("1. Logout");
        System.out.println("2. Delete User Account");
        System.out.println("3. Manage Events");
        System.out.println("4. Manage Tickets");
        System.out.println("5. Manage Venues");
        System.out.println("6. Manage Artists");
        System.out.println("7. Manage Athletes");
        System.out.println("0. Exit");
        System.out.println("====================");

        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                controller.logout();
                System.out.println("Logged out successfully.");
                break;
            case "2":
                AccountAction.handleDeleteUserAccount(scanner, controller);
                break;
            case "3":
                AdminEventMenu.display(scanner, controller);
                break;
            case "4":
                AdminTicketMenu.display(scanner, controller);
                break;
            case "5":
                AdminVenueMenu.display(scanner, controller);
                break;
            case "6":
                AdminArtistMenu.display(scanner, controller);
                break;
            case "7":
                AdminAthleteMenu.display(scanner, controller);
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