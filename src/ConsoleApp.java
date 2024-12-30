import model.*;
import presentation.admin.AdminMenu;
import presentation.CustomerMenu;
import presentation.LoginMenu;
import presentation.StartMenu;
import presentation.MainMenu;
import repository.factory.RepositoryFactory;
import service.*;
import controller.*;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepositoryFactory repositoryFactory = StartMenu.select(scanner);

        Controller controller = initializeController(repositoryFactory);

        boolean running = true;

        while (running) {
            if (controller.getAllUsers().isEmpty()) {
                running = MainMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() == null) {
                running = LoginMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Admin) {
                running = AdminMenu.display(scanner, controller);
            } else if (controller.getCurrentUser() instanceof Customer) {
                running = CustomerMenu.display(scanner, controller);
            }
        }
        scanner.close();
    }

    private static Controller initializeController(RepositoryFactory repositoryFactory) {

        // Service
        ArtistService artistService = new ArtistService(repositoryFactory);
        AthleteService athleteService = new AthleteService(repositoryFactory);
        SeatService seatService = new SeatService(repositoryFactory);
        RowService rowService = new RowService(repositoryFactory, seatService);
        SectionService sectionService = new SectionService(repositoryFactory, rowService);
        VenueService venueService = new VenueService(repositoryFactory, sectionService);
        EventService eventService = new EventService(repositoryFactory, venueService);
        TicketService ticketService = new TicketService(repositoryFactory, seatService, venueService);
        CartService cartService = new CartService(repositoryFactory);
        CustomerService customerService = new CustomerService();
        PaymentService paymentService = new PaymentService();
        PurchaseHistoryService purchaseHistoryService = new PurchaseHistoryService(repositoryFactory);
        UserService userService = new UserService(repositoryFactory, customerService);

        // Controller
        ArtistController artistController = new ArtistController(artistService);
        AthleteController athleteController = new AthleteController(athleteService);
        SeatController seatController = new SeatController(seatService);
        RowController rowController = new RowController(rowService);
        SectionController sectionController = new SectionController(sectionService);
        VenueController venueController = new VenueController(venueService);
        TicketController ticketController = new TicketController(ticketService);
        CartController cartController = new CartController(cartService);
        CustomerController customerController = new CustomerController(customerService);
        EventController eventController = new EventController(eventService);
        PaymentController paymentController = new PaymentController(paymentService);
        PurchaseHistoryController purchaseHistoryController = new PurchaseHistoryController(purchaseHistoryService);
        UserController userController = new UserController(userService);

        // Main Controller
        return new Controller(artistController, athleteController, seatController, rowController, sectionController,
                venueController, ticketController, cartController, customerController, eventController,
                paymentController, purchaseHistoryController, userController);
    }
}