package model;

import service.TicketService;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a concert event, which is a type of event featuring a list of artists.
 */
@Entity
@Table(name = "concert")
public class Concert extends Event {

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertLineUp> concertLineUps;

    public List<ConcertLineUp> getConcertLineUps() {
        return concertLineUps;
    }

    public void setConcertLineUps(List<ConcertLineUp> concertLineUps) {
        this.concertLineUps = concertLineUps;
    }

    public void addArtistToLineUp(Artist artist, Integer lineupOrder) {
        ConcertLineUp concertLineUp = new ConcertLineUp(this, artist);
        concertLineUps.add(concertLineUp);
    }

    public void removeArtistFromLineUp(Artist artist) {
        concertLineUps.removeIf(lineUp -> lineUp.getArtist().equals(artist));
    }

    public Concert() {}

    public Concert(int eventID, String eventName, String eventDescription, LocalDateTime startDateTime,
                   LocalDateTime endDateTime, int venueID, EventStatus eventStatus) {
        super(eventID, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

    @Override
    public String toString() {
        return "Concert{" +
                ", " + super.toString() +
                '}';
    }

    @Override
    public String toCsv() {
        return getID() + "," +
                getEventName() + "," +
                getEventDescription() + "," +
                getStartDateTime() + "," +
                getEndDateTime() + "," +
                getVenueID() + "," +
                getEventStatus();
    }

    public static Concert fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String eventName = fields[1];
        String eventDescription = fields[2];
        LocalDateTime startDateTime = LocalDateTime.parse(fields[3]);
        LocalDateTime endDateTime = LocalDateTime.parse(fields[4]);
        int venueID = Integer.parseInt(fields[5]);
        EventStatus eventStatus = EventStatus.valueOf(fields[6]);
        return new Concert(id, eventName, eventDescription, startDateTime, endDateTime, venueID, eventStatus);
    }

}
