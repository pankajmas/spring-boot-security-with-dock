package com.example.demo.entitytest;


import com.example.demo.controller.UserController;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.serviceimpl.ServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private ServiceImpl servicelayer;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserController userController;

    @Test
    void testCreateUser() throws UserAlreadyExistsException {
        // Arrange
        User user = new User();

        user.setFirstName("pankaj");
        user.setLastName("maske");
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("pankajmaske@gmail.com");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(encoder.encode(any())).thenReturn("abcbc");
        when(servicelayer.addUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<User> responseEntity = userController.createUser(user);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());

        verify(encoder, Mockito.times(1)).encode(any());

        // Verify that roleRepository.save() is called for each role
        verify(roleRepository, Mockito.times(1)).save(any(Role.class));

        // Verify that servicelayer.addUser() is called once
        verify(servicelayer, Mockito.times(1)).addUser(user);
    }

    @Test
    void testCreateUser_UsernameExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existingUsername");

        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

        // Act and Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userController.createUser(existingUser);
        });

        assertEquals("Username is already taken!", exception.getMessage());

        // Verify that the repository method was called
        verify(userRepository, times(1)).existsByUsername(existingUser.getUsername());
    }

    @Test
    void testCreateUser_EmailExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userRepository.existsByEmail(existingUser.getEmail())).thenReturn(true);

        // Act and Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userController.createUser(existingUser);
        });

        assertEquals("Email is already in use!", exception.getMessage());

        // Verify that the repository methods were called
        verify(userRepository, times(1)).existsByEmail(existingUser.getEmail());
    }

    @Test
    void testPartialUpdateUser_Success() {
        // Arrange
        long userId = 1L;
        User user = new User();
        user.setFirstName("pankaj");
        user.setLastName("maske");
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("pankajmaske@gmail.com");

        User updates = new User();
        updates.setFirstName("UpdatedFirstName");

        when(servicelayer.singleUser(userId)).thenReturn(java.util.Optional.of(user));
        when(servicelayer.updateUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<User> responseEntity = userController.partialUpdateUser(userId, updates);

        // Assert
        verify(servicelayer, times(1)).singleUser(userId);
        verify(servicelayer, times(1)).updateUser(user);

        assertAll(() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(user, responseEntity.getBody()));
    }

    @Test
    void testPartialUpdateUser_UserNotFound() {
        // Arrange
        long userId = 1L;
        User updates = new User();
        updates.setFirstName("ca");

        when(servicelayer.singleUser(userId)).thenReturn(java.util.Optional.empty());

        // Act
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.partialUpdateUser(userId, updates);
        });

        // Assert
        verify(servicelayer, times(1)).singleUser(userId);
        verify(servicelayer, never()).updateUser(any(User.class));

        assertEquals("User not found exception", exception.getMessage());
    }


    @Test
    void testRetrieveAll_NoUsersFound() {
        // Arrange
        when(servicelayer.retieveAll()).thenReturn(null);

        // Act
        // Assert
        assertThrows(UserNotFoundException.class, () -> userController.retrieveAll());
    }

}

