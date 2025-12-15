package be.ucll.fs.project.controller;

import be.ucll.fs.project.unit.model.Venue;
import be.ucll.fs.project.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Venue> getVenueByName(@PathVariable String name) {
        return ResponseEntity.ok(venueService.getVenueByName(name));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<Venue>> getVenuesByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(venueService.getVenuesByCity(cityId));
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<Venue>> getVenuesByMinimumCapacity(@PathVariable Integer capacity) {
        return ResponseEntity.ok(venueService.getVenuesByMinimumCapacity(capacity));
    }

    @PostMapping
    public ResponseEntity<Venue> createVenue(@Valid @RequestBody Venue venue) {
        Venue createdVenue = venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @Valid @RequestBody Venue venue) {
        Venue updatedVenue = venueService.updateVenue(id, venue);
        return ResponseEntity.ok(updatedVenue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
