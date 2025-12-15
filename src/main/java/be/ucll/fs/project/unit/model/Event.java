package be.ucll.fs.project.unit.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eventId")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @NotBlank(message = "Event title is required")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotNull(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventDescription eventDescription;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "event_venues",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "venue_id")
    )
    private List<Venue> venues = new ArrayList<>();

    public Event() {
    }

    public Event(String title, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        validateTimes();
    }

    private void validateTimes() {
        if (endTime != null && startTime != null && !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        validateTimes();
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        validateTimes();
    }

    public EventDescription getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(EventDescription eventDescription) {
        this.eventDescription = eventDescription;
        if (eventDescription != null) {
            eventDescription.setEvent(this);
        }
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public void addVenue(Venue venue) {
        this.venues.add(venue);
        venue.getEvents().add(this);
    }

    public void removeVenue(Venue venue) {
        this.venues.remove(venue);
        venue.getEvents().remove(this);
    }
}
