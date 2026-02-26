package be.ucll.fs.project.service;

import be.ucll.fs.project.exception.ResourceNotFoundException;
import be.ucll.fs.project.exception.ValidationException;
import be.ucll.fs.project.unit.model.Event;
import be.ucll.fs.project.unit.model.Ticket;
import be.ucll.fs.project.unit.model.User;
import be.ucll.fs.project.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventService eventService, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        userService.getUserById(userId); // Verify user exists
        return ticketRepository.findByUserUserId(userId);
    }

    public List<Ticket> getTicketsByEventId(Long eventId) {
        eventService.getEventById(eventId); // Verify event exists
        return ticketRepository.findByEventEventId(eventId);
    }

    public Long getAvailableTicketsForEvent(Long eventId) {
        Event event = eventService.getEventById(eventId);
        
        if (event.getAvailableTickets() == null) {
            return null; // Unlimited tickets
        }
        
        Long soldTickets = ticketRepository.countTicketsByEventId(eventId);
        return event.getAvailableTickets() - soldTickets;
    }

    public Ticket purchaseTicket(Long userId, Long eventId, Double price) {
        return purchaseTicket(userId, eventId, price, null);
    }

    public Ticket purchaseTicket(Long userId, Long eventId, Double price, String seatNumber) {
        // Verify user and event exist
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        // Validate event hasn't passed
        if (event.getEventDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Cannot purchase tickets for past events");
        }

        // Check if tickets are available
        if (event.getAvailableTickets() != null) {
            Long availableTickets = getAvailableTicketsForEvent(eventId);
            if (availableTickets <= 0) {
                throw new ValidationException("No tickets available for this event");
            }
        }

        // Validate price
        if (price <= 0) {
            throw new ValidationException("Ticket price must be positive");
        }

        // Create and save ticket
        Ticket ticket = new Ticket(user, event, price, seatNumber);
        return ticketRepository.save(ticket);
    }

    public void cancelTicket(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        
        // Optional: Add business logic for refund policy
        // For example, can't cancel if event is within 24 hours
        Event event = ticket.getEvent();
        LocalDate eventDate = event.getEventDate();
        if (eventDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new ValidationException("Cannot cancel tickets less than 24 hours before the event");
        }
        
        ticketRepository.delete(ticket);
    }

    public List<Ticket> getUserTicketsForEvent(Long userId, Long eventId) {
        userService.getUserById(userId); // Verify user exists
        eventService.getEventById(eventId); // Verify event exists
        return ticketRepository.findByUserIdAndEventId(userId, eventId);
    }
}
