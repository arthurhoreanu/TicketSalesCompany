package model;

import java.time.LocalDateTime;

/**
 * Represents a ticket for an event, including details about the event, seat, price, purchaser, and sale status.
 */
public class Ticket implements Identifiable {
    private int ticketID;
    private Event event;
    private Section section;
    private Seat seat;
    private double price;
    private LocalDateTime purchaseDate;
    private String purchaserName;
    private TicketType ticketType;
    private boolean isSold;

    /**
     * Creates a Ticket object from a CSV-formatted string.
     *
     * @param csvLine the CSV-formatted string.
     * @return the deserialized Ticket object.
     */
    public static Ticket fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int ticketID = Integer.parseInt(fields[0].trim());
        Event event = Event.fromCsv(fields[1].trim());
        Section section = Section.fromCsv(fields[2].trim());
        Seat seat = Seat.fromCsv(fields[3].trim());
        double price = Double.parseDouble(fields[4].trim());
        TicketType ticketType = TicketType.valueOf(fields[5].trim());
        boolean isSold = Boolean.parseBoolean(fields[6].trim());
        String purchaserName = fields[7].trim().equals("null") ? null : fields[7].trim();
        LocalDateTime purchaseDate = fields[8].trim().equals("null") ? null : LocalDateTime.parse(fields[8].trim());

        Ticket ticket = new Ticket(ticketID, event, section, seat, price, ticketType);
        ticket.setSold(isSold);
        ticket.setPurchaserName(purchaserName);
        ticket.setPurchaseDate(purchaseDate);

        return ticket;
    }

    /**
     * Converts the Ticket object into a CSV-formatted string.
     *
     * @return the CSV-formatted string representing the Ticket.
     */
    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(ticketID),
                event.toCsv(),
                section.toCsv(),
                seat.toCsv(),
                String.valueOf(price),
                ticketType.name(),
                String.valueOf(isSold),
                purchaserName == null ? "null" : purchaserName,
                purchaseDate == null ? "null" : purchaseDate.toString()
        );
    }



    /**
     * Constructs a Ticket with the specified attributes.
     * @param ticketID   the unique ID of the ticket
     * @param event      the event associated with the ticket
     * @param section    the section where the seat is located
     * @param seat       the seat assigned to the ticket
     * @param price      the price of the ticket
     * @param ticketType the type of the ticket (STANDARD, VIP, EARLY_ACCESS)
     */
    public Ticket(int ticketID, Event event, Section section, Seat seat, double price, TicketType ticketType) {
        this.ticketID = ticketID;
        this.event = event;
        this.section = section;
        this.seat = seat;
        this.price = price;
        this.ticketType = ticketType;
        this.isSold = false; // the ticket is initially not sold
    }

    /**
     * Gets the unique ID of the ticket.
     * @return the ID of the ticket
     */
    @Override
    public Integer getID() {
        return this.ticketID;
    }

    /**
     * Gets the event associated with this ticket.
     * @return the event of the ticket
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event associated with this ticket.
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Gets the section where the ticket's seat is located.
     * @return the section of the ticket's seat
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section for the ticket's seat.
     * @param section the section to set
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Gets the seat assigned to this ticket.
     * @return the seat of the ticket
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * Sets the seat for the ticket.
     * @param seat the seat to set
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    /**
     * Gets the price of the ticket.
     * @return the price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Checks if the ticket is sold.
     * @return true if the ticket is sold, false otherwise
     */
    public boolean isSold() {
        return isSold;
    }

    /**
     * Sets the sold status of the ticket.
     * @param sold the sold status to set
     */
    public void setSold(boolean sold) {
        isSold = sold;
    }

    /**
     * Gets the ID of the associated event.
     * @return the ID of the event
     */
    public int getEventId() {
        return event.getID(); // Retrieves the ID of the associated event
    }

    /**
     * Marks the ticket as sold and records the purchaser's name and purchase date.
     * @param purchaserName the name of the purchaser
     */
    public void markAsSold(String purchaserName) {
        this.isSold = true;
        this.purchaserName = purchaserName;
        this.purchaseDate = LocalDateTime.now();
    }


    /**
     * Sets the name of the purchaser who bought the ticket.
     *
     * @param purchaserName the name of the purchaser.
     */
    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }


    /**
     * Sets the date and time when the ticket was purchased.
     *
     * @param purchaseDate the purchase date and time.
     */
    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    /**
     * Returns a string representation of the ticket, including ID, event, section, seat, price, and sale details.
     * @return a string representing the ticket's details
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID=" + ticketID +
                ", event=" + event +
                ", section=" + section +
                ", seat=" + seat +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", purchaserName='" + purchaserName + '\'' +
                ", ticketType=" + ticketType +
                ", isSold=" + isSold +
                '}';
    }
}
