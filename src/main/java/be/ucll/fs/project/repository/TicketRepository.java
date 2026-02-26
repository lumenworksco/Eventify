package be.ucll.fs.project.repository;

import be.ucll.fs.project.unit.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByUserUserId(Long userId);
    
    List<Ticket> findByEventEventId(Long eventId);
    
    @Query("SELECT t FROM Ticket t WHERE t.user.userId = :userId AND t.event.eventId = :eventId")
    List<Ticket> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.event.eventId = :eventId")
    Long countTicketsByEventId(@Param("eventId") Long eventId);
}
