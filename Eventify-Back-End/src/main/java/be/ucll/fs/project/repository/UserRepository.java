package be.ucll.fs.project.repository;

import be.ucll.fs.project.unit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByName(String name);
    
    List<User> findByCityCityId(Long cityId);
    
    List<User> findByEventPreference(String eventPreference);
    
    List<User> findByLocation(String location);
}
