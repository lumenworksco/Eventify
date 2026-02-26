package be.ucll.fs.project.unit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateValidCity_HappyPath() {
        // Arrange & Act
        City city = new City("Brussels", "Brussels-Capital", "Belgium");

        // Assert
        assertNotNull(city);
        assertEquals("Brussels", city.getName());
        assertEquals("Brussels-Capital", city.getRegion());
        assertEquals("Belgium", city.getCountry());
    }

    @Test
    void testCityGettersAndSetters_HappyPath() {
        // Arrange
        City city = new City();

        // Act
        city.setCityId(1L);
        city.setName("Antwerp");
        city.setRegion("Flanders");
        city.setCountry("Belgium");

        // Assert
        assertEquals(1L, city.getCityId());
        assertEquals("Antwerp", city.getName());
        assertEquals("Flanders", city.getRegion());
        assertEquals("Belgium", city.getCountry());
    }

    @Test
    void testCityWithBlankName_UnhappyPath() {
        // Arrange
        City city = new City("", "Region", "Country");

        // Act
        Set<ConstraintViolation<City>> violations = validator.validate(city);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("City name is required")));
    }

    @Test
    void testCityWithNullName_UnhappyPath() {
        // Arrange
        City city = new City(null, "Region", "Country");

        // Act
        Set<ConstraintViolation<City>> violations = validator.validate(city);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("City name is required")));
    }

    @Test
    void testCityWithBlankCountry_UnhappyPath() {
        // Arrange
        City city = new City("Brussels", "Region", "");

        // Act
        Set<ConstraintViolation<City>> violations = validator.validate(city);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Country is required")));
    }

    @Test
    void testCityWithNullCountry_UnhappyPath() {
        // Arrange
        City city = new City("Brussels", "Region", null);

        // Act
        Set<ConstraintViolation<City>> violations = validator.validate(city);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Country is required")));
    }

    @Test
    void testCityWithNullRegion_HappyPath() {
        // Arrange
        City city = new City("Brussels", null, "Belgium");

        // Act
        Set<ConstraintViolation<City>> violations = validator.validate(city);

        // Assert - Region is optional, so no violations expected
        assertTrue(violations.isEmpty());
        assertNull(city.getRegion());
    }

    @Test
    void testCityCollections_HappyPath() {
        // Arrange
        City city = new City("Ghent", "Flanders", "Belgium");

        // Assert - Collections should be initialized
        assertNotNull(city.getUsers());
        assertNotNull(city.getVenues());
        assertTrue(city.getUsers().isEmpty());
        assertTrue(city.getVenues().isEmpty());
    }
}
