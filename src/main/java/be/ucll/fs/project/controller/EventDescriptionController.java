package be.ucll.fs.project.controller;

import be.ucll.fs.project.unit.model.EventDescription;
import be.ucll.fs.project.service.EventDescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-descriptions")
public class EventDescriptionController {

    private final EventDescriptionService eventDescriptionService;

    @Autowired
    public EventDescriptionController(EventDescriptionService eventDescriptionService) {
        this.eventDescriptionService = eventDescriptionService;
    }

    @GetMapping
    public ResponseEntity<List<EventDescription>> getAllEventDescriptions() {
        return ResponseEntity.ok(eventDescriptionService.getAllEventDescriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDescription> getEventDescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(eventDescriptionService.getEventDescriptionById(id));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventDescription> getEventDescriptionByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventDescriptionService.getEventDescriptionByEventId(eventId));
    }

    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<EventDescription>> getEventDescriptionsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(eventDescriptionService.getEventDescriptionsByType(eventType));
    }

    @PostMapping
    public ResponseEntity<EventDescription> createEventDescription(@Valid @RequestBody EventDescription eventDescription) {
        EventDescription createdDescription = eventDescriptionService.createEventDescription(eventDescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDescription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDescription> updateEventDescription(
            @PathVariable Long id, @Valid @RequestBody EventDescription eventDescription) {
        EventDescription updatedDescription = eventDescriptionService.updateEventDescription(id, eventDescription);
        return ResponseEntity.ok(updatedDescription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventDescription(@PathVariable Long id) {
        eventDescriptionService.deleteEventDescription(id);
        return ResponseEntity.noContent().build();
    }
}
