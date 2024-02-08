package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entitydto.UserDto;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.servicelayer.Servicelayer;

@RestController
@RequestMapping("/api")
public class UserController {

	private final Servicelayer serviceLayer;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Autowired
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

        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
        	
        	
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        Set<Role> roles = user.getRoles();

        if (roles != null) {
            // Iterate over roles and save them if they are not already saved
            for (Role role : roles) {
                if (role.getId() == null) {
                    // Role is not saved, save it
                    roleRepository.save(role);
                }
            }
        }

        user.setPassword(encoder.encode(user.getPassword()));
        // Now save the user
        User user2 = serviceLayer.addUser(user);
        return new ResponseEntity<>(user2, HttpStatus.CREATED);
    }







	@PatchMapping("/users/patch_user/{id}")
	public ResponseEntity<User> partialUpdateUser(@PathVariable long id, @RequestBody User updates) {
		User existingUser = serviceLayer.singleUser(id)
				.orElseThrow(() -> new UserNotFoundException("User not found exception"));

		// Apply partial updates from the request body
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

		User updatedUser = serviceLayer.updateUser(existingUser);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@PutMapping("/users/update_user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User u) {
		User existingUser = serviceLayer.singleUser(id)
				.orElseThrow(() -> new UserNotFoundException("User not found exception"));

		existingUser.setUsername(u.getUsername());
		existingUser.setFirstName(u.getFirstName());
		existingUser.setLastName(u.getLastName());
		existingUser.setEmail(u.getEmail());

		User updatedUser = serviceLayer.updateUser(existingUser);

		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@GetMapping("/singleuser/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR','USER')")
	public ResponseEntity<Optional<User>> singleUser(@PathVariable long id) {
		Optional<User> user = serviceLayer.singleUser(id);

		// Check if the user is present in the Optional
		if (user.isPresent()) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			throw new UserNotFoundException("User not found with this id: " + id);
		}
	}

	@GetMapping("/fetchall")
	public ResponseEntity<Map<String, Object>> retrieveAll() {

		List<User> users = serviceLayer.retieveAll();

		if (users == null) {
			throw new UserNotFoundException("Users not found");
		}

		int userCount = users.size();

		Map<String, Object> response = new HashMap<>();
		response.put("users", users);
		response.put("userCount", userCount);

		// You can customize the response further if needed
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/users_delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteSingleData(@PathVariable long id) {
		Optional<User> u = serviceLayer.singleUser(id);

		if (u.isPresent()) {
			serviceLayer.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			throw new UserNotFoundException("Please enter an existing id");
		}
	}

}
