package presentation.menus.admin;
import controller.Controller;
import presentation.actions.EventAction;

import java.util.Scanner;

public class AdminEventMenu {
    public static void display(Scanner scanner, Controller controller) {
        boolean inEventMenu = true;
        while (inEventMenu) {
            System.out.println("==== Event Management ====");
            System.out.println("1. Create Event");
            System.out.println("2. View Events");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("0. Back to Admin Menu");
            System.out.println("==========================");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    EventAction.handleCreateEvent(scanner, controller);
                    break;
                case "2":
                    EventAction.handleViewEvents(controller);
                    break;
                case "3":
                    EventAction.handleUpdateEvent(scanner, controller);
                    break;
                case "4":
                    EventAction.handleDeleteEvent(scanner, controller);
                    break;
                case "0":
                    inEventMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }
}