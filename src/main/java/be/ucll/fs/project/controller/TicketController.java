package be.ucll.fs.project.controller;

import be.ucll.fs.project.dto.TicketDTO;
import be.ucll.fs.project.unit.model.Ticket;
import be.ucll.fs.project.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getTicketsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId));
    }

    @GetMapping("/event/{eventId}/available")
    public ResponseEntity<Map<String, Object>> getAvailableTickets(@PathVariable Long eventId) {
        Long availableTickets = ticketService.getAvailableTicketsForEvent(eventId);
        return ResponseEntity.ok(Map.of(
            "eventId", eventId,
            "availableTickets", availableTickets != null ? availableTickets : "unlimited"
        ));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketService.purchaseTicket(ticketDTO.getUserId(), ticketDTO.getEventId(), 
                                                     ticketDTO.getPrice(), ticketDTO.getSeatNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.ok(Map.of("message", "Ticket cancelled successfully"));
    }

    @GetMapping("/user/{userId}/event/{eventId}")
    public ResponseEntity<List<Ticket>> getUserTicketsForEvent(
            @PathVariable Long userId, 
            @PathVariable Long eventId) {
        return ResponseEntity.ok(ticketService.getUserTicketsForEvent(userId, eventId));
    }
}
