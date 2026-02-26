package be.ucll.fs.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventDescriptionDTO {

    @NotBlank(message = "Event type cannot be blank")
    @Size(max = 100, message = "Event type must be less than 100 characters")
    private String eventType;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;

    public EventDescriptionDTO() {
    }

    public EventDescriptionDTO(String eventType, String description, Long eventId) {
        this.eventType = eventType;
        this.description = description;
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
