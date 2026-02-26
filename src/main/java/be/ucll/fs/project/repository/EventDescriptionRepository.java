package be.ucll.fs.project.repository;

import be.ucll.fs.project.unit.model.EventDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventDescriptionRepository extends JpaRepository<EventDescription, Long> {
    
    Optional<EventDescription> findByEventEventId(Long eventId);
    
    List<EventDescription> findByEventType(String eventType);
}
