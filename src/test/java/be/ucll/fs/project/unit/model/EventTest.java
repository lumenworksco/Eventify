package be.ucll.fs.project.unit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateValidEvent_HappyPath() {
        // Arrange & Act
        LocalDate eventDate = LocalDate.of(2025, 12, 20);
        LocalTime startTime = LocalTime.of(18, 0);
        LocalTime endTime = LocalTime.of(23, 0);
        
        Event event = new Event("Rock Festival", eventDate, startTime, endTime);

        // Assert
        assertNotNull(event);
        assertEquals("Rock Festival", event.getTitle());
        assertEquals(eventDate, event.getEventDate());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
    }

    @Test
    void testEventGettersAndSetters_HappyPath() {
        // Arrange
        Event event = new Event();
        LocalDate date = LocalDate.of(2025, 12, 25);
        LocalTime start = LocalTime.of(20, 0);
        LocalTime end = LocalTime.of(23, 30);

        // Act
        event.setEventId(1L);
        event.setTitle("Jazz Night");
        event.setEventDate(date);
        event.setStartTime(start);
        event.setEndTime(end);
        event.setAvailableTickets(100);

        // Assert
        assertEquals(1L, event.getEventId());
        assertEquals("Jazz Night", event.getTitle());
        assertEquals(date, event.getEventDate());
        assertEquals(start, event.getStartTime());
        assertEquals(end, event.getEndTime());
        assertEquals(100, event.getAvailableTickets());
    }

    @Test
    void testEventWithBlankTitle_UnhappyPath() {
        // Arrange
        Event event = new Event("", LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(22, 0));

        // Act
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Event title is required")));
    }

    @Test
    void testEventWithNullTitle_UnhappyPath() {
        // Arrange
        Event event = new Event(null, LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(22, 0));

        // Act
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Event title is required")));
    }

    @Test
    void testEventWithNullDate_UnhappyPath() {
        // Arrange
        Event event = new Event("Concert", null, LocalTime.of(18, 0), LocalTime.of(22, 0));

        // Act
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Event date is required")));
    }

    @Test
    void testEventWithNullStartTime_UnhappyPath() {
        // Arrange
        Event event = new Event("Concert", LocalDate.now(), null, LocalTime.of(22, 0));

        // Act
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Start time is required")));
    }

    @Test
    void testEventWithNullEndTime_UnhappyPath() {
        // Arrange
        Event event = new Event("Concert", LocalDate.now(), LocalTime.of(18, 0), null);

        // Act
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("End time is required")));
    }

    @Test
    void testEventVenuesCollection_HappyPath() {
        // Arrange
        Event event = new Event("Festival", LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(23, 0));

        // Assert - Venues collection should be initialized
        assertNotNull(event.getVenues());
        assertTrue(event.getVenues().isEmpty());
    }

    @Test
    void testEventWithAvailableTickets_HappyPath() {
        // Arrange
        Event event = new Event("Theater Show", LocalDate.now(), LocalTime.of(19, 0), LocalTime.of(21, 0));

        // Act
        event.setAvailableTickets(250);

        // Assert
        assertEquals(250, event.getAvailableTickets());
    }

    @Test
    void testEventWithNullAvailableTickets_HappyPath() {
        // Arrange
        Event event = new Event("Open Air Concert", LocalDate.now(), LocalTime.of(16, 0), LocalTime.of(23, 0));

        // Assert - Available tickets can be null (unlimited)
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertTrue(violations.isEmpty());
        assertNull(event.getAvailableTickets());
    }
}
