package be.ucll.fs.project.service;

import be.ucll.fs.project.unit.model.Event;
import be.ucll.fs.project.unit.model.EventDescription;
import be.ucll.fs.project.unit.model.Venue;
import be.ucll.fs.project.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final VenueService venueService;
    private final EventDescriptionService eventDescriptionService;

    @Autowired
    public EventService(EventRepository eventRepository, VenueService venueService, 
                       EventDescriptionService eventDescriptionService) {
        this.eventRepository = eventRepository;
        this.venueService = venueService;
        this.eventDescriptionService = eventDescriptionService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
    }

    public Event getEventByTitle(String title) {
        return eventRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with title: " + title));
    }

    public List<Event> getEventsByDate(LocalDate date) {
        return eventRepository.findByEventDate(date);
    }

    public List<Event> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByEventDateBetween(startDate, endDate);
    }

    public List<Event> getEventsByCity(Long cityId) {
        return eventRepository.findEventsByCityId(cityId);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDate.now());
    }

    public Event createEvent(Event event, List<Long> venueIds) {
        if (venueIds == null || venueIds.isEmpty()) {
            throw new IllegalArgumentException("Event must have at least one venue");
        }
        
        Event savedEvent = eventRepository.save(event);
        
        for (Long venueId : venueIds) {
            Venue venue = venueService.getVenueById(venueId);
            savedEvent.addVenue(venue);
        }
        
        return eventRepository.save(savedEvent);
    }

    public Event updateEvent(Long id, Event eventDetails, List<Long> venueIds) {
        Event event = getEventById(id);
        
        event.setTitle(eventDetails.getTitle());
        event.setEventDate(eventDetails.getEventDate());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        
        if (venueIds != null && !venueIds.isEmpty()) {
            event.getVenues().clear();
            for (Long venueId : venueIds) {
                Venue venue = venueService.getVenueById(venueId);
                event.addVenue(venue);
            }
        }
        
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    public Event addVenueToEvent(Long eventId, Long venueId) {
        Event event = getEventById(eventId);
        Venue venue = venueService.getVenueById(venueId);
        event.addVenue(venue);
        return eventRepository.save(event);
    }

    public Event removeVenueFromEvent(Long eventId, Long venueId) {
        Event event = getEventById(eventId);
        Venue venue = venueService.getVenueById(venueId);
        event.removeVenue(venue);
        
        if (event.getVenues().isEmpty()) {
            throw new IllegalArgumentException("Event must have at least one venue");
        }
        
        return eventRepository.save(event);
    }

    public List<Event> getEventsByType(String eventType) {
        return eventRepository.findByEventType(eventType);
    }

    public List<String> getAllEventTypes() {
        return eventRepository.findAllEventTypes();
    }
}
