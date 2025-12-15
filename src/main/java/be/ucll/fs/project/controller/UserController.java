package be.ucll.fs.project.controller;

import be.ucll.fs.project.unit.model.User;
import be.ucll.fs.project.service.UserService;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
