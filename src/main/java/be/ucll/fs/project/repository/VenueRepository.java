package be.ucll.fs.project.repository;

import be.ucll.fs.project.unit.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    
    Optional<Venue> findByName(String name);
    
    List<Venue> findByCityCityId(Long cityId);
    
    List<Venue> findByCapacityGreaterThanEqual(Integer capacity);
}
