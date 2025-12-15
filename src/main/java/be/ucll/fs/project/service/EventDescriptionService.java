package be.ucll.fs.project.service;

import be.ucll.fs.project.unit.model.Event;
import be.ucll.fs.project.unit.model.EventDescription;
import be.ucll.fs.project.repository.EventDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventDescriptionService {

    private final EventDescriptionRepository eventDescriptionRepository;

    @Autowired
    public EventDescriptionService(EventDescriptionRepository eventDescriptionRepository) {
        this.eventDescriptionRepository = eventDescriptionRepository;
    }

    public List<EventDescription> getAllEventDescriptions() {
        return eventDescriptionRepository.findAll();
    }

    public EventDescription getEventDescriptionById(Long id) {
        return eventDescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event description not found with id: " + id));
    }

    public EventDescription getEventDescriptionByEventId(Long eventId) {
        return eventDescriptionRepository.findByEventEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event description not found for event id: " + eventId));
    }

    public List<EventDescription> getEventDescriptionsByType(String eventType) {
        return eventDescriptionRepository.findByEventType(eventType);
    }

    public EventDescription createEventDescription(EventDescription eventDescription) {
        return eventDescriptionRepository.save(eventDescription);
    }

    public EventDescription updateEventDescription(Long id, EventDescription descriptionDetails) {
        EventDescription description = getEventDescriptionById(id);
        
        description.setEventType(descriptionDetails.getEventType());
        description.setFeaturedArtists(descriptionDetails.getFeaturedArtists());
        description.setTicketPurchaseLink(descriptionDetails.getTicketPurchaseLink());
        description.setExtraDescription(descriptionDetails.getExtraDescription());
        
        return eventDescriptionRepository.save(description);
    }

    public void deleteEventDescription(Long id) {
        EventDescription description = getEventDescriptionById(id);
        eventDescriptionRepository.delete(description);
    }
}
