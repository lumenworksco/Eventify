package be.ucll.fs.project.unit.service;

import be.ucll.fs.project.repository.CityRepository;
import be.ucll.fs.project.service.CityService;
import be.ucll.fs.project.unit.model.City;
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
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void testGetAllCities_ReturnsAllCities() {
        // Arrange
        City city1 = new City("Brussels", "Brussels-Capital", "Belgium");
        City city2 = new City("Antwerp", "Flanders", "Belgium");
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        // Act
        List<City> cities = cityService.getAllCities();

        // Assert
        assertEquals(2, cities.size());
        assertEquals("Brussels", cities.get(0).getName());
        assertEquals("Antwerp", cities.get(1).getName());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testGetCityById_ReturnsCityWhenFound() {
        // Arrange
        City city = new City("Ghent", "Flanders", "Belgium");
        city.setCityId(1L);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        // Act
        City result = cityService.getCityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Ghent", result.getName());
        assertEquals("Belgium", result.getCountry());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCityByName_ReturnsCityWhenFound() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        when(cityRepository.findByName("Brussels")).thenReturn(Optional.of(city));

        // Act
        City result = cityService.getCityByName("Brussels");

        // Assert
        assertNotNull(result);
        assertEquals("Brussels", result.getName());
        assertEquals("Brussels-Capital", result.getRegion());
        verify(cityRepository, times(1)).findByName("Brussels");
    }

    @Test
    void testGetCitiesByCountry_ReturnsCitiesInCountry() {
        // Arrange
        City city1 = new City("Brussels", "Brussels-Capital", "Belgium");
        City city2 = new City("Antwerp", "Flanders", "Belgium");
        when(cityRepository.findByCountry("Belgium")).thenReturn(Arrays.asList(city1, city2));

        // Act
        List<City> cities = cityService.getCitiesByCountry("Belgium");

        // Assert
        assertEquals(2, cities.size());
        assertTrue(cities.stream().allMatch(c -> c.getCountry().equals("Belgium")));
        verify(cityRepository, times(1)).findByCountry("Belgium");
    }

    @Test
    void testGetCitiesByRegion_ReturnsCitiesInRegion() {
        // Arrange
        City city1 = new City("Antwerp", "Flanders", "Belgium");
        City city2 = new City("Ghent", "Flanders", "Belgium");
        when(cityRepository.findByRegion("Flanders")).thenReturn(Arrays.asList(city1, city2));

        // Act
        List<City> cities = cityService.getCitiesByRegion("Flanders");

        // Assert
        assertEquals(2, cities.size());
        assertTrue(cities.stream().allMatch(c -> c.getRegion().equals("Flanders")));
        verify(cityRepository, times(1)).findByRegion("Flanders");
    }

    @Test
    void testCreateCity_SavesAndReturnsCity() {
        // Arrange
        City newCity = new City("Leuven", "Flanders", "Belgium");
        when(cityRepository.existsByName("Leuven")).thenReturn(false);
        when(cityRepository.save(newCity)).thenReturn(newCity);

        // Act
        City result = cityService.createCity(newCity);

        // Assert
        assertNotNull(result);
        assertEquals("Leuven", result.getName());
        verify(cityRepository, times(1)).existsByName("Leuven");
        verify(cityRepository, times(1)).save(newCity);
    }

    @Test
    void testUpdateCity_UpdatesAndReturnsCity() {
        // Arrange
        City existingCity = new City("Brussels", "Brussels-Capital", "Belgium");
        existingCity.setCityId(1L);
        
        City updatedDetails = new City("Brussels", "Brussels Region", "Belgium");
        
        when(cityRepository.findById(1L)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(any(City.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        City result = cityService.updateCity(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Brussels Region", result.getRegion());
        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void testDeleteCity_DeletesCitySuccessfully() {
        // Arrange
        City city = new City("TestCity", "TestRegion", "TestCountry");
        city.setCityId(1L);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        doNothing().when(cityRepository).delete(city);

        // Act
        cityService.deleteCity(1L);

        // Assert
        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).delete(city);
    }
}
