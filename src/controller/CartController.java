package controller;

import model.Cart;
import model.Customer;
import model.Event;
import model.Ticket;
import service.CartService;

import java.util.List;

/**
 * The CartController class provides methods to manage the cart of the current customer.
 * It allows adding, removing, clearing tickets, finalizing the cart, and retrieving cart details.
 */
public class CartController {
    private final CartService cartService;

    /**
     * Constructs a CartController with the specified CartService.
     *
     * @param cartService the service for cart-related operations
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Creates a new cart for a customer and an associated event.
     *
     * @param customer The customer for whom the cart is created.
     * @param event    The event associated with the cart.
     * @return The newly created cart.
     */
    public Cart createCart(Customer customer, Event event) {
        Cart cart = cartService.createCart(customer, event);
        System.out.println("New cart created for customer: " + customer.getUsername() + " and event: " + event.getEventName());
        return cart;
    }

    /**
     * Adds a ticket to the specified cart.
     *
     * @param cart   The cart to which the ticket is added.
     * @param ticket The ticket to add to the cart.
     */
    public void addTicketToCart(Cart cart, Ticket ticket) {
        boolean isAdded = cartService.addTicketToCart(cart, ticket);
        if (isAdded) {
            System.out.println("Ticket successfully added to the cart.");
        } else {
            System.out.println("Failed to add ticket to the cart. Ensure it belongs to the same event.");
        }
    }

    /**
     * Removes a ticket from the specified cart.
     *
     * @param cart   The cart from which the ticket is removed.
     * @param ticket The ticket to remove.
     */
    public void removeTicketFromCart(Cart cart, Ticket ticket) {
        boolean isRemoved = cartService.removeTicketFromCart(cart, ticket);
        if (isRemoved) {
            System.out.println("Ticket successfully removed from the cart.");
        } else {
            System.out.println("Ticket not found in the cart.");
        }
    }

    /**
     * Clears all tickets from the specified cart.
     *
     * @param cart The cart to clear.
     */
    public void clearCart(Cart cart) {
        cartService.clearCart(cart);
        System.out.println("Cart has been cleared.");
    }

    /**
     * Finalizes the specified cart and converts it into a purchase history.
     *
     * @param cart The cart to finalize.
     */
    public void finalizeCart(Cart cart) {
        try {
            cartService.finalizeCart(cart);
            System.out.println("Cart has been finalized and converted to purchase history.");
        } catch (IllegalStateException e) {
            System.out.println("Error finalizing cart: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.out.println("Feature not yet implemented: " + e.getMessage());
        }
    }

    /**
     * Retrieves all tickets in the specified cart.
     *
     * @param cart The cart for which to retrieve tickets.
     * @return A list of tickets in the cart.
     */
    public List<Ticket> getTicketsInCart(Cart cart) {
        List<Ticket> tickets = cartService.getTicketsInCart(cart);
        if (tickets.isEmpty()) {
            System.out.println("The cart is empty.");
        } else {
            System.out.println("Tickets in cart:");
            tickets.forEach(ticket -> System.out.println(ticket));
        }
        return tickets;
    }

    /**
     * Finds a cart by its ID.
     *
     * @param cartID The ID of the cart to find.
     * @return The cart if found, or null otherwise.
     */
    public Cart findCartByID(int cartID) {
        Cart cart = cartService.findCartByID(cartID);
        if (cart != null) {
            System.out.println("Cart found: " + cart);
        } else {
            System.out.println("Cart with ID " + cartID + " not found.");
        }
        return cart;
    }
}