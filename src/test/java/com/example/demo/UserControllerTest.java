//package com.example.demo;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import java.util.HashSet;
//import java.util.Set;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import com.example.demo.controller.UserController;
//import com.example.demo.entity.ERole;
//import com.example.demo.entity.Role;
//import com.example.demo.entity.User;
//import com.example.demo.exception.UserAlreadyExistsException;
//import com.example.demo.repository.RoleRepository;
//import com.example.demo.servicelayer.Servicelayer;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//	@Mock
//	private Servicelayer servicelayer;
//
//	@Mock
//	private RoleRepository roleRepository;
//
//	@Mock
//	private PasswordEncoder encoder;
//
//	@InjectMocks
//	private UserController userController;
//
//	@Test
//	public void testCreateUser() throws UserAlreadyExistsException {
//		// Arrange
//		User user = new User();
//
//		user.setFirstName("pankaj");
//		user.setLastName("maske");
//		user.setUsername("testUser");
//		user.setPassword("testPassword");
//		user.setEmail("pankajmaske@gmail.com");
//
//		Role role = new Role();
//		role.setName(ERole.ROLE_USER);
//
//		Set<Role> roles = new HashSet<>();
//		roles.add(role);
//		user.setRoles(roles);
//
//		when(roleRepository.save(any(Role.class))).thenReturn(role);
//		when(encoder.encode(any())).thenReturn("encodepassword");
//		when(servicelayer.addUser(any(User.class))).thenReturn(user);
//
//		// Act
//		ResponseEntity<UserDTo> responseEntity = userController.createUser(user);
//
//		// Assert
//		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//		assertEquals(user, responseEntity.getBody());
//
//		// Verify that roleRepository.save() is called for each role
//		verify(roleRepository, Mockito.times(1)).save(any(Role.class));
//
//		// Verify that servicelayer.addUser() is called once
//		verify(servicelayer, Mockito.times(1)).addUser(user);
//	}
//}
