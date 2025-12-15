package be.ucll.fs.project.repository;

import be.ucll.fs.project.unit.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    Optional<City> findByName(String name);
    
    List<City> findByCountry(String country);
    
    List<City> findByRegion(String region);
    
    boolean existsByName(String name);
}
