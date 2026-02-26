package be.ucll.fs.project.unit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;
    private City testCity;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testCity = new City("Brussels", "Brussels-Capital", "Belgium");
    }

    @Test
    void testCreateValidUser_HappyPath() {
        // Arrange & Act
        User user = new User("John Doe", "hashedPassword123", Role.USER, "Downtown", "Music", testCity);

        // Assert
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("hashedPassword123", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertEquals("Downtown", user.getLocation());
        assertEquals("Music", user.getEventPreference());
        assertEquals(testCity, user.getCity());
    }

    @Test
    void testUserGettersAndSetters_HappyPath() {
        // Arrange
        User user = new User();

        // Act
        user.setUserId(1L);
        user.setName("Jane Smith");
        user.setPassword("securePass");
        user.setRole(Role.ADMIN);
        user.setLocation("Uptown");
        user.setEventPreference("Theater");
        user.setCity(testCity);

        // Assert
        assertEquals(1L, user.getUserId());
        assertEquals("Jane Smith", user.getName());
        assertEquals("securePass", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals("Uptown", user.getLocation());
        assertEquals("Theater", user.getEventPreference());
        assertEquals(testCity, user.getCity());
    }

    @Test
    void testUserWithBlankName_UnhappyPath() {
        // Arrange
        User user = new User("", "password", Role.USER, "Location", "Preference", testCity);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("User name is required")));
    }

    @Test
    void testUserWithNullPassword_UnhappyPath() {
        // Arrange
        User user = new User("John Doe", null, Role.USER, "Location", "Preference", testCity);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    void testUserWithNullCity_UnhappyPath() {
        // Arrange
        User user = new User("John Doe", "password", Role.USER, "Location", "Preference", null);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("City is required")));
    }

    @Test
    void testUserWithNullRole_UnhappyPath() {
        // Arrange
        User user = new User("John Doe", "password", null, "Location", "Preference", testCity);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Role is required")));
    }

    @Test
    void testUserWithDifferentRoles_HappyPath() {
        // Test ADMIN role
        User admin = new User("Admin User", "pass", Role.ADMIN, "Loc", "Pref", testCity);
        assertEquals(Role.ADMIN, admin.getRole());

        // Test ORGANIZER role
        User organizer = new User("Organizer User", "pass", Role.ORGANIZER, "Loc", "Pref", testCity);
        assertEquals(Role.ORGANIZER, organizer.getRole());

        // Test USER role
        User regularUser = new User("Regular User", "pass", Role.USER, "Loc", "Pref", testCity);
        assertEquals(Role.USER, regularUser.getRole());
    }
}
