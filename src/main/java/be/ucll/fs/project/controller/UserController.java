package be.ucll.fs.project.controller;

import be.ucll.fs.project.dto.LoginRequest;
import be.ucll.fs.project.dto.LoginResponse;
import be.ucll.fs.project.dto.UserDTO;
import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.unit.model.Role;
import be.ucll.fs.project.unit.model.User;
import be.ucll.fs.project.service.UserService;
import be.ucll.fs.project.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByName(loginRequest.getName());
        
        if (!userService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String token = jwtUtil.generateToken(user.getName(), user.getUserId(), user.getRole().name());
        LoginResponse response = new LoginResponse(token, user.getUserId(), user.getName(), user.getRole().name());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        String role = (String) request.getAttribute("userRole");
        
        List<User> users = userService.getAllUsers();
        
        // ADMIN sees all user details
        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(users);
        }
        
        // ORGANIZER and USER see limited information (no passwords, just names and preferences)
        var limitedUsers = users.stream()
            .map(u -> java.util.Map.of(
                "userId", u.getUserId(),
                "name", u.getName(),
                "eventPreference", u.getEventPreference() != null ? u.getEventPreference() : "",
                "role", u.getRole().name()
            ))
            .toList();
        
        return ResponseEntity.ok(limitedUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserByName(name));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<User>> getUsersByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(userService.getUsersByCity(cityId));
    }

    @GetMapping("/preference/{eventPreference}")
    public ResponseEntity<List<User>> getUsersByEventPreference(@PathVariable String eventPreference) {
        return ResponseEntity.ok(userService.getUsersByEventPreference(eventPreference));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<User>> getUsersByLocation(@PathVariable String location) {
        return ResponseEntity.ok(userService.getUsersByLocation(location));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        City city = new City();
        city.setCityId(userDTO.getCityId());
        Role role = userDTO.getRole() != null ? userDTO.getRole() : Role.USER;
        User user = new User(userDTO.getName(), userDTO.getPassword(), 
                            role, 
                            userDTO.getLocation(), userDTO.getEventPreference(), city);
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        City city = new City();
        city.setCityId(userDTO.getCityId());
        Role role = userDTO.getRole() != null ? userDTO.getRole() : Role.USER;
        User user = new User(userDTO.getName(), userDTO.getPassword(), 
                            role, 
                            userDTO.getLocation(), userDTO.getEventPreference(), city);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
