package be.ucll.fs.project.service;

import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.unit.model.Venue;
import be.ucll.fs.project.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;
    private final CityService cityService;

    @Autowired
    public VenueService(VenueRepository venueRepository, CityService cityService) {
        this.venueRepository = venueRepository;
        this.cityService = cityService;
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + id));
    }

    public Venue getVenueByName(String name) {
        return venueRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with name: " + name));
    }

    public List<Venue> getVenuesByCity(Long cityId) {
        return venueRepository.findByCityCityId(cityId);
    }

    public List<Venue> getVenuesByMinimumCapacity(Integer capacity) {
        return venueRepository.findByCapacityGreaterThanEqual(capacity);
    }

    public Venue createVenue(Venue venue) {
        if (venue.getCity() != null && venue.getCity().getCityId() != null) {
            City city = cityService.getCityById(venue.getCity().getCityId());
            venue.setCity(city);
        }
        return venueRepository.save(venue);
    }

    public Venue updateVenue(Long id, Venue venueDetails) {
        Venue venue = getVenueById(id);
        
        venue.setName(venueDetails.getName());
        venue.setAddress(venueDetails.getAddress());
        venue.setCapacity(venueDetails.getCapacity());
        
        if (venueDetails.getCity() != null && venueDetails.getCity().getCityId() != null) {
            City city = cityService.getCityById(venueDetails.getCity().getCityId());
            venue.setCity(city);
        }
        
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        Venue venue = getVenueById(id);
        venueRepository.delete(venue);
    }
}
