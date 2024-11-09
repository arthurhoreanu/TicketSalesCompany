package presentation.actions;

import controller.Controller;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class EventAction {
    // Arthur's TODO on hold - depends on other services
//    public static void handleCreateEvent(Scanner scanner, Controller controller) {
//        System.out.println("=== Create Event ===");
//        System.out.print("Enter Event Type (Concert/Sports Event): ");
//        String eventType = scanner.nextLine();
//
//        System.out.print("Enter event name: ");
//        String eventName = scanner.nextLine();
//        System.out.print("Enter event description: ");
//        String eventDescription = scanner.nextLine();
//        System.out.print("Enter start date and time (YYYY-MM-DDTHH:MM): ");
//        LocalDateTime startDateTime = LocalDateTime.parse(scanner.nextLine());
//        System.out.print("Enter end date and time (YYYY-MM-DDTHH:MM): ");
//        LocalDateTime endDateTime = LocalDateTime.parse(scanner.nextLine());
//
//        System.out.print("Enter venue name: ");
//        String venueName = scanner.nextLine();
//        Venue venue = controller.findVenueByName(venueName); // Retrieve the Venue object based on user input
//
//        // Set default status and empty ticket list for this example
//        EventStatus eventStatus = EventStatus.SCHEDULED; // Default status
//        List<Ticket> tickets = new ArrayList<>(); // Initialize an empty ticket list
//
//        if ("Concert".equalsIgnoreCase(eventType)) {
//            handleConcertCreation(scanner, controller, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
//        } else if ("Sports Event".equalsIgnoreCase(eventType)) {
//            handleSportsEventCreation(scanner, controller, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets);
//        } else {
//            System.out.println("Invalid event type. Please enter 'Concert' or 'Sports Event'.");
//        }
//    }
//
//    // Helper method for creating a Concert event
//    private static void handleConcertCreation(Scanner scanner, Controller controller, String eventName, String eventDescription,
//                                              LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus,
//                                              List<Ticket> tickets) {
//        System.out.print("Enter artist name: ");
//        String artistName = scanner.nextLine();
//        Artist artist = controller.findArtistByName(artistName); // Assume a method in the controller to get an Artist by name
//
//        System.out.print("Enter genre: ");
//        String genre = scanner.nextLine();
//
//        // Generate a unique event ID
//        int eventId = controller.generateEventId(); // Assume this method exists to create unique IDs
//
//        // Call the controller to create a Concert event
//        controller.createConcert(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, artist, genre);
//        System.out.println("Concert created successfully.");
//    }
//
//    // Helper method for creating a Sports Event
//    private static void handleSportsEventCreation(Scanner scanner, Controller controller, String eventName, String eventDescription,
//                                                  LocalDateTime startDateTime, LocalDateTime endDateTime, Venue venue, EventStatus eventStatus,
//                                                  List<Ticket> tickets) {
//        System.out.print("Enter sport name: ");
//        String sportName = scanner.nextLine();
//
//        System.out.print("Enter number of athletes participating: ");
//        int numAthletes = Integer.parseInt(scanner.nextLine());
//        List<Athlete> athletes = new ArrayList<>();
//        for (int i = 0; i < numAthletes; i++) {
//            System.out.print("Enter athlete " + (i + 1) + " name: ");
//            String athleteName = scanner.nextLine();
//            Athlete athlete = controller.findAthleteByName(athleteName); // Assume a method in the controller to get an Athlete by name
//            athletes.add(athlete);
//        }
//
//        // Generate a unique event ID
//        int eventId = controller.generateEventId(); // Assume this method exists to create unique IDs
//
//        // Call the controller to create a SportsEvent
//        controller.createSportsEvent(eventId, eventName, eventDescription, startDateTime, endDateTime, venue, eventStatus, tickets, athletes, sportName);
//        System.out.println("Sports Event created successfully.");
//    }

    // Method to handle viewing all events
    public static void handleViewEvents(Controller controller) {
        System.out.println("=== View Events ===");
        List<Event> events = controller.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }
    }

    // Method to handle updating an event
    public static void handleUpdateEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Update Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to update: ");
        int eventId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new event name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new event description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new start date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime newStartDateTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter new end date and time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime newEndDateTime = LocalDateTime.parse(scanner.nextLine());

        // Prompting for new event status
        System.out.print("Enter new event status (SCHEDULED, CANCELLED, COMPLETED): ");
        String statusInput = scanner.nextLine().toUpperCase();
        EventStatus newStatus;

        try {
            newStatus = EventStatus.valueOf(statusInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter one of SCHEDULED, CANCELLED, or COMPLETED.");
            return;
        }
    }

    // Method to handle deleting an event
    public static void handleDeleteEvent(Scanner scanner, Controller controller) {
        System.out.println("=== Delete Event ===");

        List<Event> events = controller.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event event : events) {
                System.out.println(event);
            }
        }

        System.out.print("Enter Event ID to delete: ");
        int eventId = Integer.parseInt(scanner.nextLine());

    }
}