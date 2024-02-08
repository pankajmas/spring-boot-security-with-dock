package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class IntegrationTest {

	@Test
	void contextLoads() {
	}

	@Test
	public void testJsonToObject() throws Exception {

		// Your JSON template
		String jsonTemplate = "{\"id\":%d,\"username\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"roles\":[{\"id\":%d,\"name\":\"%s\"}]}";

		// Creating a JSON string with specific values
		String json = String.format(jsonTemplate, 1L, "john_doe", "John", "Doe", "john.doe@example.com", "password123",
				1L, "ROLE_USER");

		ObjectMapper mapper = new ObjectMapper();
		User expectedUser = mapper.readValue(json, User.class);
		// Create an instance with the same values for comparison
		User actualUser = new User();
		actualUser.setId(1L);
		actualUser.setUsername("john_doe");
		actualUser.setFirstName("John");
		actualUser.setLastName("Doe");
		actualUser.setEmail("john.doe@example.com");
		actualUser.setPassword("password123");

		// Set roles if needed
		Role roleUser = new Role();
		roleUser.setId(1L);
		roleUser.setName(ERole.ROLE_USER);

		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		actualUser.setRoles(roles);

		// Compare the actual and expected objects
		assertEquals(expectedUser.getId(), actualUser.getId());
		assertEquals(expectedUser.getUsername(), actualUser.getUsername());
		assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
		assertEquals(expectedUser.getLastName(), actualUser.getLastName());
		assertEquals(expectedUser.getEmail(), actualUser.getEmail());
		assertEquals(expectedUser.getPassword(), actualUser.getPassword());
		assertEquals((expectedUser.getRoles()),(actualUser.getRoles()));

	}
}
