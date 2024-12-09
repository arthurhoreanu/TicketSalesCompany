package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract event with a unique ID, name, description, schedule, venue, and status.
 */
public abstract class Event implements Identifiable {
    private int eventID;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Venue venue;
    private EventStatus eventStatus;
    static Controller controller = ControllerProvider.getController();

    /**
     * Constructs an Event with the specified details.
     * @param eventID          the unique ID of the event
     * @param eventName        the name of the event
     * @param eventDescription a description of the event
     * @param startDateTime    the start date and time of the event
     * @param endDateTime      the end date and time of the event
     * @param venue            the venue where the event takes place
     * @param eventStatus      the current status of the event (SCHEDULED, CANCELLED, COMPLETED)
     */
    public Event(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                 LocalDateTime endDateTime, Venue venue, EventStatus eventStatus) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
        this.eventStatus = eventStatus;
    }

    /**
     * Gets the unique ID of the event.
     * @return the event ID
     */
    @Override
    public Integer getID() {
        return this.eventID;
    }

    /**
     * Gets the name of the event.
     * @return the name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets a new name for the event.
     * @param eventName the new name of the event
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the description of the event.
     * @return the description of the event
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets a new description for the event.
     * @param eventDescription the new description of the event
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the start date and time of the event.
     * @return the start date and time of the event
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets a new start date and time for the event.
     * @param startDateTime the new start date and time of the event
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time of the event.
     * @return the end date and time of the event
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets a new end date and time for the event.
     * @param endDateTime the new end date and time of the event
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the venue where the event is held.
     * @return the venue of the event
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Sets a new venue for the event.
     * @param venue the new venue for the event
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Gets the current status of the event (SCHEDULED, CANCELLED, COMPLETED).
     * @return the status of the event
     */
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    /**
     * Sets a new status for the event.
     * @param eventStatus the new status of the event
     */
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public static Event fromDatabase(ResultSet rs, Controller controller) throws SQLException {
        int id = rs.getInt("eventID");
        String type = rs.getString("eventType");
        String eventName = rs.getString("eventName");
        String eventDescription = rs.getString("eventDescription");
        LocalDateTime startDateTime = rs.getTimestamp("startDateTime").toLocalDateTime();
        LocalDateTime endDateTime = rs.getTimestamp("endDateTime").toLocalDateTime();
        String venueName = rs.getString("venueName");
        EventStatus status = EventStatus.valueOf(rs.getString("eventStatus"));

        Venue venue = controller.findVenueByName(venueName);
        if (venue == null) {
            throw new IllegalArgumentException("Venue with name " + venueName + " not found.");
        }

        switch (type) {
            case "Concert":
                return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venue, status);
            case "Sports Event":
                return new SportsEvent(id, eventName, eventDescription, startDateTime, endDateTime, venue, status);
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }

    /**
     * Returns a string representation of the event, including its ID, name, description, schedule, venue, and status.
     * @return a string representing the event's details
     */
    @Override
    public String toString() {
        return "{" + eventID + ", eventName=" + eventName + ", eventDescription=" + eventDescription +
                ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", venue=" + venue +
                ", eventStatus=" + eventStatus + '}';
    }

    /**
     * Parses a CSV line to create an Event object: either a Concert or a Sports Event.
     * The method uses the provided CSV data to populate the fields of the event,
     * including associated artists or athletes for the specific event type.
     *
     * @param csvLine A single line from a CSV file representing an event. The fields are expected in the following order:
     * ID (integer),
     * Type (String: "Concert" or "Sports Event"),
     * Event Name (String),
     * Event Description (String),
     * Start Date and Time (String in ISO_LOCAL_DATE_TIME format),
     * End Date and Time (String in ISO_LOCAL_DATE_TIME format)
     * Venue name (String)
     * EventStatus
     * Related entity names (String, semicolon-separated list of artist or athlete names)
     *
     * @return An Event object corresponding to the provided CSV data. The returned object will be an instance of
     * Concert SportsEvent depending on the event type.
     * @throws IllegalArgumentException If the event type is not recognized.
     */

    public static Event fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0].trim());
        String type = fields[1].trim();
        String eventName = fields[2].trim();
        String eventDescription = fields[3].trim();
        LocalDateTime startDateTime = LocalDateTime.parse(fields[4].trim());
        LocalDateTime endDateTime = LocalDateTime.parse(fields[5].trim());
        String venueName = fields[6].trim();
        EventStatus status = EventStatus.valueOf(fields[7].trim());

        Venue venue = controller.findVenueByName(venueName);
        if (venue == null) {
            throw new IllegalArgumentException("Venue with ID " + venueName + " not found.");
        }

        switch (type) {
            case "Concert": {
                return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venue, status);
            }
            case "Sports Event": {
                return new SportsEvent(id, eventName, eventDescription, startDateTime, endDateTime, venue, status);
            }
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }

}