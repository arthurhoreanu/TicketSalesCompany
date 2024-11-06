package model;

public class Section {
    private int sectionID;
    private String sectionName;
    private int sectionCapacity;
    private Venue venue;

    public Section(int sectionID, String sectionName, int sectionCapacity, Venue venue) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionCapacity = sectionCapacity;
        this.venue = venue;
    }

    public Venue venue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionCapacity() {
        return sectionCapacity;
    }

    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

    // TODO method to return the list of available seats
    // public List<Seat> getAvailableSeats() {}
}
