package service;

import exception.BusinessLogicException;
import exception.EntityNotFoundException;
import exception.ValidationException;
import model.*;
import repository.IRepository;
import repository.factory.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VenueService {
    private final IRepository<Venue> venueRepository;
    private final IRepository<Section> sectionRepository;
    private final IRepository<Row> rowRepository;
    private final IRepository<Seat> seatRepository;

    public VenueService(RepositoryFactory venueFactory, RepositoryFactory sectionFactory,
                        RepositoryFactory rowFactory, RepositoryFactory seatFactory) {
        this.venueRepository = venueFactory.createVenueRepository();
        this.sectionRepository = sectionFactory.createSectionRepository();
        this.rowRepository = rowFactory.createRowRepository();
        this.seatRepository = seatFactory.createSeatRepository();
    }

    // Seat

    public void updateSeat(Seat seat) {
        if (seat == null) {
            throw new ValidationException("Seat cannot be null.");
        }
        if (findRowByID(seat.getRow().getID()) == null) {
            throw new BusinessLogicException("Row associated with the seat does not exist.");
        }
        seatRepository.update(seat);
    }

    /**
     * Creates a new Seat and saves it to all repositories.
     *
     * @param rowId      the ID of the Row to which the Seat belongs.
     * @param seatNumber the number of the Seat.
     * @return the created Seat object.
     */
    public Seat createSeat(int rowId, int seatNumber) {
        Row row = findRowByID(rowId);
        if (row == null) {
            throw new EntityNotFoundException("Row not found");
        }
        Seat seat = new Seat(0, seatNumber, false, row);
        row.addSeat(seat);
        seatRepository.create(seat);
        return seat;
    }

    public void deleteSeatsByRow(int rowId) {
        List<Seat> seats = seatRepository.getAll().stream()
                .filter(seat -> seat.getRow().getID() == rowId)
                .toList();
        for (Seat seat : seats) {
            seatRepository.delete(seat.getID());
        }
    }

    /**
     * Deletes a Seat by its ID.
     *
     * @param seatID the ID of the Seat to delete.
     * @return true if the Seat was successfully deleted, false otherwise.
     */
    public boolean deleteSeat(int seatID) {
        Seat seat = findSeatByID(seatID);
        if (seat == null) {
            throw new EntityNotFoundException("Seat not found");
        }
        Row row = seat.getRow();
        if (row != null) {
            row.removeSeat(seat);
        }
        seatRepository.delete(seatID);
        return true;
    }

    /**
     * Retrieves a Seat by its ID.
     *
     * @param seatId the ID of the Seat to retrieve.
     * @return the Seat object, or null if not found.
     */
    public Seat findSeatByID(int seatId) {
        return seatRepository.read(seatId);
    }

    /**
     * Retrieves all Seats from the repository.
     *
     * @return a list of all Seats.
     */
    public List<Seat> getAllSeats() {
        return seatRepository.getAll();
    }

    /**
     * Reserves a Seat by associating it with a Ticket.
     *
     * @param seatId the ID of the Seat to reserve.
     * @return true if the Seat was successfully reserved, false otherwise.
     */
    public boolean reserveSeat(int seatId, Event event, Customer customer, double price, TicketType ticketType) {
        Seat seat = findSeatByID(seatId);
        if (seat.isReserved()) {
            throw new ValidationException("Seat with ID " + seatId + " is already reserved.");
        }
        if (event == null) {
            throw new ValidationException("Event cannot be null.");
        }
        if (customer == null) {
            throw new ValidationException("Customer cannot be null.");
        }
        // Create a Ticket object and associate it with the Seat
        Ticket ticket = new Ticket(0, event, seat, customer, price, ticketType);
        seat.setReserved(true);
        seat.setTicket(ticket);
        // Update the seat in the repositories
        seatRepository.update(seat);
        return true;
    }

    /**
     * Unreserves a Seat, removing its association with any Ticket.
     *
     * @param seatId the ID of the Seat to unreserve.
     */
    public void unreserveSeat(int seatId) {
        Seat seat = findSeatByID(seatId);
        if (seat == null || !seat.isReserved()) {
            throw new ValidationException("Seat is not reserved.");
        }
        seat.setReserved(false);
        seat.setTicket(null);
        seatRepository.update(seat);
    }

    /**
     * Checks if a Seat is reserved for a specific Event.
     *
     * @param seatId  the ID of the Seat.
     * @param eventId the ID of the Event.
     * @return true if the Seat is reserved for the Event, false otherwise.
     */
    public boolean isSeatReservedForEvent(int seatId, int eventId) {
        Seat seat = findSeatByID(seatId);
        return seat != null && seat.isReserved()
                && seat.getTicket() != null
                && seat.getTicket().getEvent().getID() == eventId;
    }

    public boolean isSeatForEvent(Seat seat, int eventId) {
        return seat.getTicket() != null && seat.getTicket().getEvent().getID() == eventId;
    }

    // Row

    public Row createRow(Section section, int rowCapacity) {
        if (section == null) {
            throw new BusinessLogicException("Section cannot be null.");
        }
        Row row = new Row(0, rowCapacity, section);
        rowRepository.create(row);
        section.addRow(row);
        return row;
    }

    public Row updateRow(int rowId, int rowCapacity) {
        Row row = findRowByID(rowId);
        if (row == null) {
            throw new EntityNotFoundException("Row not found with ID: " + rowId);
        }
        row.setRowCapacity(rowCapacity);
        rowRepository.update(row);
        return row;
    }

    public void deleteRowsBySection(int sectionID) {
        List<Row> rows = rowRepository.getAll().stream().
                filter(row -> row.getSection().getID() == sectionID)
                .toList();
        for (Row row : rows) {
            deleteSeatsByRow(row.getID());
            rowRepository.delete(row.getID());
        }
    }

    public void deleteRow(int rowID) {
        Row row = findRowByID(rowID);
        if (row == null) {
            throw new EntityNotFoundException("Row not found with ID: " + rowID);
        }
        deleteSeatsByRow(rowID);
        rowRepository.delete(rowID);
    }

    public Row findRowByID(int rowId) {
        return rowRepository.read(rowId);
    }

    public List<Row> getAllRows() {
        return rowRepository.getAll();
    }

    /**
     * Adds a specified number of seats to a row.
     *
     * @param rowId         the ID of the row to which seats will be added
     * @param numberOfSeats the number of seats to add
     * @throws EntityNotFoundException if the row with the specified ID is not found
     */
    public void addSeatsToRow(int rowId, int numberOfSeats) {
        Row row = findRowByID(rowId);
        if (row == null) {
            throw new EntityNotFoundException("Row not found");
        }
        for (int i = 1; i <= numberOfSeats; i++) {
            Seat seat = new Seat(0, i, false, row); // Create the Seat object
            row.addSeat(seat); // Add the Seat to the Row's seats list
            seatRepository.create(seat); // Persist the Seat in the repository
        }

        rowRepository.update(row); // Persist the updated Row with its seats
    }

    /**
     * Finds rows within a specified section.
     *
     * @param sectionId the ID of the section
     * @return a list of rows in the specified section, or an empty list if the section is not found
     */
    public List<Row> findRowsBySection(int sectionId) {
        Section section = findSectionByID(sectionId);
        return section != null ? section.getRows() : new ArrayList<>();
    }

    /**
     * Retrieves the available (non-reserved) seats in a specific row for a given event.
     *
     * @param rowId   the ID of the row
     * @param eventId the ID of the event
     * @return a list of available seats in the specified row and event, or an empty list if the row is not found
     */
    public List<Seat> getAvailableSeatsInRow(int rowId, int eventId) {
        Row row = findRowByID(rowId);
        if (row == null) {
            return new ArrayList<>();
        }
        return row.getSeats().stream()
                .filter(seat -> !seat.isReserved() && isSeatForEvent(seat, eventId))
                .collect(Collectors.toList());
    }

    /**
     * Recommends the closest available seat to the selected seats in a specific row and section.
     *
     * @param sectionId           the ID of the section
     * @param rowId               the ID of the row
     * @param selectedSeatNumbers a list of seat numbers that have already been selected
     * @return the closest available seat, or null if no suitable seat is found
     */
    public Seat recommendClosestSeat(int sectionId, int rowId, List<Integer> selectedSeatNumbers) {
        Section section = findSectionByID(sectionId);
        if (section == null) {
            return null;
        }

        Row row = section.getRows().stream()
                .filter(r -> r.getID() == rowId)
                .findFirst()
                .orElse(null);
        if (row == null) {
            return null;
        }

        List<Seat> availableSeats = row.getSeats().stream()
                .filter(seat -> !seat.isReserved())
                .filter(seat -> !selectedSeatNumbers.contains(seat.getNumber()))
                .toList();

        if (availableSeats.isEmpty()) {
            return null;
        }

        List<Seat> sortedSeats = availableSeats.stream()
                .sorted((seat1, seat2) -> {
                    int minDistanceToSeat1 = selectedSeatNumbers.stream()
                            .mapToInt(selectedSeat -> Math.abs(seat1.getNumber() - selectedSeat))
                            .min().orElse(Integer.MAX_VALUE);

                    int minDistanceToSeat2 = selectedSeatNumbers.stream()
                            .mapToInt(selectedSeat -> Math.abs(seat2.getNumber() - selectedSeat))
                            .min().orElse(Integer.MAX_VALUE);

                    return Integer.compare(minDistanceToSeat1, minDistanceToSeat2);
                })
                .toList();

        return sortedSeats.get(0);
    }

    public List<Seat> getSeatsByRow(int rowId) {
        Row row = findRowByID(rowId);
        return (row != null) ? row.getSeats() : new ArrayList<>();
    }

    // Section

    public Section createSection(Venue venue, int sectionCapacity, String sectionName) {
        if (venue == null) {
            throw new BusinessLogicException("Venue cannot be null.");
        }
        if (venue.getSections().stream().anyMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName))) {
            throw new BusinessLogicException("Section with name '" + sectionName + "' already exists in the venue.");
        }
        Section section = new Section();// ID will be auto-assigned
        section.setSectionName(sectionName);
        section.setVenue(venue);
        section.setSectionCapacity(sectionCapacity);
        sectionRepository.create(section); // Persist the new section
        return section;
    }

    /**
     * Updates an existing Section.
     *
     * @param sectionId       the ID of the Section to update.
     * @param sectionName     the new name for the Section.
     * @param sectionCapacity the new capacity for the Section.
     * @return the updated Section object, or null if the Section does not exist.
     */
    public Section updateSection(int sectionId, String sectionName, int sectionCapacity) {
        Section section = sectionRepository.read(sectionId);
        if (section == null) {
            return null; // Section not found
        }
        section.setSectionName(sectionName);
        section.setSectionCapacity(sectionCapacity);
        sectionRepository.update(section);
        return section;
    }

    public void deleteSectionByVenue(int venueID) {
        List<Section> sections = sectionRepository.getAll().stream().
                filter(section -> section.getVenue().getID() == venueID)
                .toList();
        for (Section section : sections) {
            deleteRowsBySection(section.getID());
            sectionRepository.delete(section.getID());
        }
    }

    /**
     * Deletes a Section by its ID.
     *
     * @param sectionID the ID of the Section to delete.
     * @return true if the Section was successfully deleted, false otherwise.
     */
    public void deleteSection(int sectionID) {
        Section section = sectionRepository.read(sectionID);
        if (section == null) {
            throw new EntityNotFoundException("Section not found with ID: " + sectionID);
        }
        deleteRowsBySection(sectionID);
        sectionRepository.delete(sectionID);
    }

    /**
     * Retrieves a Section by its ID.
     *
     * @param sectionId the ID of the Section to retrieve.
     * @return the Section object if found, null otherwise.
     */
    public Section findSectionByID(int sectionId) {
        return sectionRepository.read(sectionId);
    }

    /**
     * Retrieves all Sections from the repository.
     *
     * @return a list of all Sections.
     */
    public List<Section> getAllSections() {
        return sectionRepository.getAll();
    }

    public void addRowsToSection(int sectionID, int numberOfRows, int rowCapacity) {
        Section section = sectionRepository.read(sectionID);
        if (section == null) {
            throw new EntityNotFoundException("Section not found");
        }
        for (int i = 0; i < numberOfRows; i++) {
            createRow(section, rowCapacity);
        }
        sectionRepository.update(section);
    }

    /**
     * Finds Sections by their name.
     *
     * @param name the name of the Sections to search for.
     * @return a list of Sections matching the given name.
     */
    public List<Section> findSectionsByName(String name) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getSectionName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available Seats in a Section for a specific Event.
     *
     * @param sectionId the ID of the Section.
     * @param eventId   the ID of the Event.
     * @return a list of available Seats in the Section.
     */
    public List<Seat> getAvailableSeatsInSection(int sectionId, int eventId) {
        Section section = sectionRepository.read(sectionId);
        if (section == null) {
            return new ArrayList<>(); // Section not found
        }
        List<Seat> availableSeats = new ArrayList<>();
        for (Row row : section.getRows()) {
            availableSeats.addAll(
                    row.getSeats().stream()
                            .filter(seat -> !seat.isReserved() && seat.getTicket() != null
                                    && seat.getTicket().getEvent().getID() == eventId)
                            .toList()
            );
        }
        return availableSeats;
    }

    // Venue

    /**
     * Creates a new Venue and saves it to both repositories.
     */
    public Venue createVenue(String name, String location, int capacity, boolean hasSeats) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessLogicException("Venue name cannot be null or empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new BusinessLogicException("Venue location cannot be null or empty.");
        }
        if (capacity <= 0) {
            throw new ValidationException("Venue capacity must be greater than zero.");
        }
        Venue venue = new Venue();
        venue.setVenueName(name);
        venue.setLocation(location);
        venue.setVenueCapacity(capacity);
        venue.setHasSeats(hasSeats);
        venueRepository.create(venue);
        if (!hasSeats) {
            addSectionToVenue(venue.getID(), 1, capacity, "Default Section");
        }
        return venue;
    }

    /**
     * Retrieves a Venue by its ID.
     */
    public Venue findVenueByID(int venueId) {
        return venueRepository.read(venueId);
    }

    /**
     * Finds Venues by location or name.
     */
    public List<Venue> findVenuesByLocationOrName(String keyword) {
        return venueRepository.getAll().stream()
                .filter(venue -> venue.getVenueName().equalsIgnoreCase(keyword) || venue.getLocation().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
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
     * Retrieves all Venues from the in-memory repository.
     */
    public List<Venue> getAllVenues() {
        return venueRepository.getAll();
    }

    /**
     * Updates an existing Venue.
     */
    public Venue updateVenue(int venueId, String name, String location, int capacity, boolean hasSeats) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            return null; // Venue not found
        }
        venue.setVenueName(name);
        venue.setLocation(location);
        venue.setVenueCapacity(capacity);
        venue.setHasSeats(hasSeats);
        venueRepository.update(venue);
        return venue;
    }

    /**
     * Deletes a Venue and its associated Sections.
     */
    public boolean deleteVenue(int venueId) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            return false;
        }
        deleteSectionByVenue(venueId);
        venueRepository.delete(venueId);
        return true;
    }

    /**
     * Adds a Section to a Venue.
     */
    public void addSectionToVenue(int venueId, int numberOfSections, int sectionCapacity, String defaultSectionName) {
        Venue venue = venueRepository.read(venueId);
        if (venue == null) {
            throw new EntityNotFoundException("Venue not found");
        }
        for (int i = 0; i < numberOfSections; i++) {
            String sectionName = defaultSectionName + " " + (i + 1);
            if (venue.getSections().stream().anyMatch(s -> s.getSectionName().equals(sectionName))) {
                throw new ValidationException("Duplicate section name: " + sectionName);
            }
            Section section = createSection(venue, sectionCapacity, sectionName);
            sectionRepository.create(section); // Persist the section in the repository
        }
        // Reload the sections for the venue
        loadSectionsForVenue(venue);
        venueRepository.update(venue); // Persist the updated venue
    }


    /**
     * Retrieves all Sections associated with a specific Venue.
     */
    public List<Section> getSectionsByVenueID(int venueId) {
        return sectionRepository.getAll().stream()
                .filter(section -> section.getVenue().getID() == venueId)
                .collect(Collectors.toList());
    }

    public void loadSectionsForVenue(Venue venue) {
        List<Section> sections = getSectionsByVenueID(venue.getID());
        venue.setSections(sections); // Populate the sections list
    }


    /**
     * Retrieves all available Seats in a Venue for a specific Event.
     */
    public List<Seat> getAvailableSeatsInVenue(int venueId, int eventId) {
        Venue venue = findVenueByID(venueId);
        if (venue == null || !venue.isHasSeats()) {
            return new ArrayList<>();
        }

        return venue.getSections().stream()
                .flatMap(section -> section.getRows().stream())
                .flatMap(row -> row.getSeats().stream())
                .filter(seat -> !seat.isReserved() && seat.getTicket() == null)
                .collect(Collectors.toList());
    }

}