package be.ucll.fs.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TicketDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private Double price;

    private String seatNumber;

    public TicketDTO() {
    }

    public TicketDTO(Long userId, Long eventId, Double price) {
        this.userId = userId;
        this.eventId = eventId;
        this.price = price;
    }

    public TicketDTO(Long userId, Long eventId, Double price, String seatNumber) {
        this.userId = userId;
        this.eventId = eventId;
        this.price = price;
        this.seatNumber = seatNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
