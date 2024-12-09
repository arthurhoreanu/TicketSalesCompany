package service;

import model.*;
import repository.FileRepository;
import repository.IRepository;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class for managing venue-related operations such as creating, updating, deleting venues,
 * and managing sections and seat availability within venues.
 */
public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final FileRepository<Venue> venueFileRepository;
    private final SectionService sectionService;

    /**
     * Constructs a VenueService with the specified dependencies.
     *
     * @param venueRepository the repository for managing Venue persistence.
     * @param sectionService  the service for managing sections within venues.
     */
    public VenueService(IRepository<Venue> venueRepository, SectionService sectionService) {
        this.venueRepository = venueRepository;
        this.sectionService = sectionService;
        this.venueFileRepository = new FileRepository<>("src/repository/data/venues.csv", Venue::fromCsv);
        List<Venue> venuesFromFile = venueFileRepository.getAll();
        for (Venue venue : venuesFromFile) {
            venueRepository.create(venue);
        }
    }

    /**
     * Adds a new venue to the repository if it doesn't already exist at the specified location.
     *
     * @param name     the name of the venue.
     * @param location the location of the venue.
     * @param capacity the capacity of the venue.
     * @param sections the list of sections within the venue.
     * @return true if the venue was added successfully, false if a duplicate venue exists.
     */
    public boolean addVenue(String name, String location, int capacity, List<Section> sections) {
        int newID = venueRepository.getAll().size() + 1;
        for (Venue venue : venueRepository.getAll()) {
            if (venue.getVenueName().equalsIgnoreCase(name) && venue.getLocation().equalsIgnoreCase(location)) {
                return false;
            }
        }
        Venue venue = new Venue(newID, name, location, capacity, sections);
        venueRepository.create(venue);
        venueFileRepository.create(venue);
        return true;
    }

    /**
     * Updates an existing venue by ID with new details and sections.
     *
     * @param id           the ID of the venue to update.
     * @param newName      the new name of the venue.
     * @param newLocation  the new location of the venue.
     * @param newCapacity  the new capacity of the venue.
     * @param newSections  the updated list of sections within the venue.
     * @return true if the venue was updated successfully, false if no such venue exists.
     */
    public boolean updateVenue(int id, String newName, String newLocation, int newCapacity, List<Section> newSections) {
        Venue venue = findVenueByID(id);
        if (venue != null) {
            venue.setVenueName(newName);
            venue.setLocation(newLocation);
            venue.setVenueCapacity(newCapacity);
            venue.sections = newSections;
            venueRepository.update(venue);
            venueFileRepository.update(venue);
            return true;
        }
        return false;
    }

    /**
     * Deletes a venue by ID.
     *
     * @param id the ID of the venue to delete.
     * @return true if the venue was successfully deleted, false if no such venue exists.
     */
    public boolean deleteVenue(int id) {
        if (findVenueByID(id) != null) {
            venueRepository.delete(id);
            venueFileRepository.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of all venues in the repository.
     *
     * @return a list of all venues.
     */
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    /**
     * Finds a venue by its ID.
     *
     * @param id the ID of the venue to find.
     * @return the Venue object if found, null otherwise.
     */
    public Venue findVenueByID(int id) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getID() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a venue by its name.
     *
     * @param name the name of the venue to find.
     * @return the Venue object if found, null otherwise.
     */
    public Venue findVenueByName(String name) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the number of available seats for a specific event in the entire venue.
     *
     * @param venue the venue to check for available seats.
     * @param event the event for which to check seat availability.
     * @return the total number of available seats in the venue for the event.
     */
    public int getAvailableSeats(Venue venue, Event event) {
        return venue.getSections().stream()
                .mapToInt(section -> sectionService.getAvailableSeats(section, event).size())
                .sum();
    }

    /**
     * Retrieves a list of available seats for a specific event in the entire venue.
     *
     * @param venue the venue to check for available seats.
     * @param event the event for which to check seat availability.
     * @return a list of available seats in the venue for the event.
     */
    public List<Seat> getAvailableSeatsList(Venue venue, Event event) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Section section : venue.getSections()) {
            availableSeats.addAll(sectionService.getAvailableSeats(section, event));
        }
        return availableSeats;
    }

    /**
     * Recommends a seat in a venue based on customer preferences and seat availability for an event.
     *
     * @param customer the customer for whom to recommend a seat.
     * @param venue    the venue in which to find a recommended seat.
     * @param event    the event for which to recommend a seat.
     * @return the recommended Seat object, or null if no preferred seat is available.
     *
     * Suppose the venue has three sections:
     *
     * Section A (ID 1), Section B (ID 2), Section C (ID 3).
     * The customer has preferences as follows:
     *
     * Section A: 5 (highest preference)
     * Section B: 3
     * Section C: 1
     * The sections are sorted so that Section A is checked first, then Section B, and finally Section C.
     *
     * If Section A has an available seat, it is immediately recommended.
     *
     * If Section A is full, the method checks Section B, and so on.
     */
    public Seat recommendSeat(Customer customer, Venue venue, Event event) {
        List<Section> sections = venue.getSections();

        sections.sort((s1, s2) -> {
            int preference1 = customer.getPreferredSections().getOrDefault(s1.getID(), 0);
            int preference2 = customer.getPreferredSections().getOrDefault(s2.getID(), 0);
            return Integer.compare(preference2, preference1);
        });

        for (Section section : sections) {
            Seat recommendedSeat = sectionService.recommendSeat(customer, section, event);
            if (recommendedSeat != null) {
                return recommendedSeat;
            }
        }
        return null; // No preferred seat available
    }

    /**
     * Retrieves a list of venues that match the specified location or name.
     *
     * @param locationOrVenueName the location or name to search for.
     * @return a list of matching venues.
     */
    public List<Venue> getVenuesByLocationOrName(String locationOrVenueName) {
        List<Venue> matchingVenues = new ArrayList<>();
        for (Venue venue : getAllVenues()) {
            if (venue.getVenueName().equalsIgnoreCase(locationOrVenueName) ||
                    venue.getLocation().equalsIgnoreCase(locationOrVenueName)) {
                matchingVenues.add(venue);
            }
        }
        return matchingVenues;
    }
}

