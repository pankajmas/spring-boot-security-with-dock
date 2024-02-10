package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.servicelayer.Servicelayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Servicelayer serviceLayer;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserController(Servicelayer serviceLayer, RoleRepository roleRepository, UserRepository userRepository,
                          PasswordEncoder encoder) {
        this.serviceLayer = serviceLayer;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserAlreadyExistsException {
        validateUser(user);

        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = serviceLayer.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PatchMapping("/users/patch_user/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable long id, @RequestBody User updates) {
        User existingUser = getUserById(id);
        applyPartialUpdates(existingUser, updates);
        User updatedUser = serviceLayer.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/update_user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User updates) {
        User existingUser = getUserById(id);
        applyUpdates(existingUser, updates);
        User updatedUser = serviceLayer.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/singleuser/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR','USER')")
    public ResponseEntity<User> singleUser(@PathVariable long id) {
        User user = getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/fetchall")
    public ResponseEntity<Map<String, Object>> retrieveAll() {
        List<User> users = serviceLayer.retieveAll();
        int userCount = users.size();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("userCount", userCount);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users_delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSingleData(@PathVariable long id) {
        getUserById(id); // This will throw exception if user doesn't exist
        serviceLayer.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Helper methods
    private User getUserById(long id) {
        return serviceLayer.singleUser(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private void validateUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }
        saveNewRolesIfNotExist(user.getRoles());
    }

    private void saveNewRolesIfNotExist(Set<Role> roles) {
        if (roles != null) {
            roles.stream()
                    .filter(role -> role.getId() == null)
                    .forEach(roleRepository::save);
        }
    }

    private void applyPartialUpdates(User existingUser, User updates) {
        if (updates.getUsername() != null) {
            existingUser.setUsername(updates.getUsername());
        }
        if (updates.getFirstName() != null) {
            existingUser.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null) {
            existingUser.setLastName(updates.getLastName());
        }
        if (updates.getEmail() != null) {
            existingUser.setEmail(updates.getEmail());
        }
    }

    private void applyUpdates(User existingUser, User updates) {
        existingUser.setUsername(updates.getUsername());
        existingUser.setFirstName(updates.getFirstName());
        existingUser.setLastName(updates.getLastName());
        existingUser.setEmail(updates.getEmail());
    }
}
