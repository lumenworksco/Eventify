package be.ucll.fs.project.controller;

import be.ucll.fs.project.unit.model.Event;
import be.ucll.fs.project.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Event> getEventByTitle(@PathVariable String title) {
        return ResponseEntity.ok(eventService.getEventByTitle(title));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getEventsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventService.getEventsByDate(date));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Event>> getEventsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(eventService.getEventsBetweenDates(startDate, endDate));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<Event>> getEventsByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(eventService.getEventsByCity(cityId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Map<String, Object> eventData) {
        Event event = mapToEvent(eventData);
        @SuppressWarnings("unchecked")
        List<Long> venueIds = (List<Long>) eventData.get("venueIds");
        Event createdEvent = eventService.createEvent(event, venueIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Map<String, Object> eventData) {
        Event event = mapToEvent(eventData);
        @SuppressWarnings("unchecked")
        List<Long> venueIds = (List<Long>) eventData.get("venueIds");
        Event updatedEvent = eventService.updateEvent(id, event, venueIds);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/venues/{venueId}")
    public ResponseEntity<Event> addVenueToEvent(@PathVariable Long eventId, @PathVariable Long venueId) {
        Event event = eventService.addVenueToEvent(eventId, venueId);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}/venues/{venueId}")
    public ResponseEntity<Event> removeVenueFromEvent(@PathVariable Long eventId, @PathVariable Long venueId) {
        Event event = eventService.removeVenueFromEvent(eventId, venueId);
        return ResponseEntity.ok(event);
    }

    private Event mapToEvent(Map<String, Object> eventData) {
        Event event = new Event();
        event.setTitle((String) eventData.get("title"));
        event.setEventDate(LocalDate.parse((String) eventData.get("eventDate")));
        event.setStartTime(java.time.LocalTime.parse((String) eventData.get("startTime")));
        event.setEndTime(java.time.LocalTime.parse((String) eventData.get("endTime")));
        return event;
    }
}
