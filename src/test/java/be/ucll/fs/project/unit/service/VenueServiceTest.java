package be.ucll.fs.project.unit.service;

import be.ucll.fs.project.repository.VenueRepository;
import be.ucll.fs.project.service.CityService;
import be.ucll.fs.project.service.VenueService;
import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.unit.model.Venue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private CityService cityService;

    @InjectMocks
    private VenueService venueService;

    @Test
    void testGetAllVenues_ReturnsAllVenues() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        Venue venue1 = new Venue("Concert Hall", "Address 1", 2000, city);
        Venue venue2 = new Venue("Stadium", "Address 2", 50000, city);
        when(venueRepository.findAll()).thenReturn(Arrays.asList(venue1, venue2));

        // Act
        List<Venue> venues = venueService.getAllVenues();

        // Assert
        assertEquals(2, venues.size());
        assertEquals("Concert Hall", venues.get(0).getName());
        assertEquals("Stadium", venues.get(1).getName());
        verify(venueRepository, times(1)).findAll();
    }

    @Test
    void testGetVenueById_ReturnsVenueWhenFound() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        Venue venue = new Venue("Arena", "Main Street 1", 15000, city);
        venue.setVenueId(1L);
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        // Act
        Venue result = venueService.getVenueById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Arena", result.getName());
        assertEquals(15000, result.getCapacity());
        verify(venueRepository, times(1)).findById(1L);
    }

    @Test
    void testGetVenueByName_ReturnsVenueWhenFound() {
        // Arrange
        City city = new City("Antwerp", "Flanders", "Belgium");
        Venue venue = new Venue("Sportpaleis", "Address", 23000, city);
        when(venueRepository.findByName("Sportpaleis")).thenReturn(Optional.of(venue));

        // Act
        Venue result = venueService.getVenueByName("Sportpaleis");

        // Assert
        assertNotNull(result);
        assertEquals("Sportpaleis", result.getName());
        assertEquals(23000, result.getCapacity());
        verify(venueRepository, times(1)).findByName("Sportpaleis");
    }

    @Test
    void testGetVenuesByCity_ReturnsVenuesInCity() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        city.setCityId(1L);
        Venue venue1 = new Venue("Venue 1", "Address 1", 1000, city);
        Venue venue2 = new Venue("Venue 2", "Address 2", 2000, city);
        when(venueRepository.findByCityCityId(1L)).thenReturn(Arrays.asList(venue1, venue2));

        // Act
        List<Venue> venues = venueService.getVenuesByCity(1L);

        // Assert
        assertEquals(2, venues.size());
        verify(venueRepository, times(1)).findByCityCityId(1L);
    }

    @Test
    void testGetVenuesByMinimumCapacity_ReturnsFilteredVenues() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        Venue venue1 = new Venue("Large Arena", "Address 1", 20000, city);
        Venue venue2 = new Venue("Mega Stadium", "Address 2", 50000, city);
        when(venueRepository.findByCapacityGreaterThanEqual(10000))
            .thenReturn(Arrays.asList(venue1, venue2));

        // Act
        List<Venue> venues = venueService.getVenuesByMinimumCapacity(10000);

        // Assert
        assertEquals(2, venues.size());
        assertTrue(venues.stream().allMatch(v -> v.getCapacity() >= 10000));
        verify(venueRepository, times(1)).findByCapacityGreaterThanEqual(10000);
    }

    @Test
    void testCreateVenue_SavesAndReturnsVenue() {
        // Arrange
        City city = new City("Ghent", "Flanders", "Belgium");
        city.setCityId(1L);
        Venue newVenue = new Venue("New Venue", "New Address", 5000, city);
        
        when(cityService.getCityById(1L)).thenReturn(city);
        when(venueRepository.save(any(Venue.class))).thenReturn(newVenue);

        // Act
        Venue result = venueService.createVenue(newVenue);

        // Assert
        assertNotNull(result);
        assertEquals("New Venue", result.getName());
        assertEquals(5000, result.getCapacity());
        verify(cityService, times(1)).getCityById(1L);
        verify(venueRepository, times(1)).save(any(Venue.class));
    }

    @Test
    void testUpdateVenue_UpdatesAndReturnsVenue() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        city.setCityId(1L);
        
        Venue existingVenue = new Venue("Old Name", "Old Address", 1000, city);
        existingVenue.setVenueId(1L);
        
        Venue updatedDetails = new Venue("New Name", "New Address", 1500, city);
        
        when(venueRepository.findById(1L)).thenReturn(Optional.of(existingVenue));
        when(cityService.getCityById(1L)).thenReturn(city);
        when(venueRepository.save(any(Venue.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Venue result = venueService.updateVenue(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(1500, result.getCapacity());
        verify(venueRepository, times(1)).findById(1L);
        verify(venueRepository, times(1)).save(any(Venue.class));
    }

    @Test
    void testDeleteVenue_DeletesVenueSuccessfully() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        Venue venue = new Venue("Delete Me", "Address", 1000, city);
        venue.setVenueId(1L);
        
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        doNothing().when(venueRepository).delete(venue);

        // Act
        venueService.deleteVenue(1L);

        // Assert
        verify(venueRepository, times(1)).findById(1L);
        verify(venueRepository, times(1)).delete(venue);
    }
}
