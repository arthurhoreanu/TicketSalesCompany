package controller;

import model.FavouriteItem;
import service.CustomerService;

import java.util.Set;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void addFavorite(FavouriteItem item) {
        boolean success = customerService.addFavorite(item);
        if (success) {
            System.out.println("Enity was marked as favourite.");
        }
    }

    public void removeFavorite(FavouriteItem item) {
        boolean success = customerService.removeFavorite(item);
        if (success) {
            System.out.println("Enity was removed from favourites.");
        }
    }

    public Set<FavouriteItem> getFavorites() {
        return customerService.getFavorites();
    }
}