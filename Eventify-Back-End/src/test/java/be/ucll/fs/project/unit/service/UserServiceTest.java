package be.ucll.fs.project.unit.service;

import be.ucll.fs.project.repository.UserRepository;
import be.ucll.fs.project.service.CityService;
import be.ucll.fs.project.service.UserService;
import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.unit.model.Role;
import be.ucll.fs.project.unit.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityService cityService;

    @InjectMocks
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers_AsAdmin() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        User user1 = new User("Alice", "hashedpass", Role.ADMIN, "Brussels", "Music", city);
        User user2 = new User("Bob", "hashedpass", Role.USER, "Antwerp", "Sports", city);
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserById_AsUser() {
        // Arrange
        City city = new City("Brussels", "Brussels-Capital", "Belgium");
        User user = new User("Alice", "hashedpass", Role.USER, "Brussels", "Music", city);
        user.setUserId(1L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "organizer", roles = {"ORGANIZER"})
    void testCreateUser_AsOrganizer() {
        // Arrange
        City city = new City("Ghent", "Flanders", "Belgium");
        city.setCityId(1L);
        
        User newUser = new User();
        newUser.setName("Charlie");
        newUser.setPassword("plainPassword");
        newUser.setCity(city);
        
        when(cityService.getCityById(1L)).thenReturn(city);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User result = userService.createUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals("Charlie", result.getName());
        assertNotEquals("plainPassword", result.getPassword()); // Password should be hashed
        assertEquals(Role.USER, result.getRole()); // Default role
        verify(userRepository, times(1)).save(any(User.class));
    }
}
