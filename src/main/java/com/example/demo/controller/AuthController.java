package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.ErrorResponse;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.jwt.services.UserDetailsImpl;
import com.example.demo.utility.AuthUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthUtil authUtil;
    
  

    @Autowired
    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authUtil = authUtil;
    }


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {

			if (authUtil.isUserTemporarilyLocked(loginRequest.getUsername())) {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
						"User is temporarily locked out for 20 minutes. Please try again later.", null));
			}

			int remainingAttempts = authUtil.getRemainingLoginAttempts(loginRequest.getUsername());
			if (remainingAttempts > 0) {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
								loginRequest.getPassword()));

				authUtil.resetLoginAttempts(loginRequest.getUsername());

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());
				
				  // Update last login time
				userDetails.setLastLoginTime(LocalDateTime.now());
//				User user = new User();
//				user.setLastLoginTime(LocalDateTime.now());
//				userRepository.save(user);
				

				return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getFirstName(),
						userDetails.getLastName(), userDetails.getUsername(), userDetails.getEmail(),userDetails.getLastLoginTime(), roles));
			} else {
				// Handle case when max attempts reached
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
						"Maximum login attempts reached. User is temporarily locked out for 20 minutes.", null));
			}
		} 
		catch (AuthenticationException e) {
			// Handle authentication failure
			authUtil.handleAuthenticationFailure(loginRequest.getUsername());
			int remainingAttempts = authUtil.getRemainingLoginAttempts(loginRequest.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
					"Authentication failed. " + remainingAttempts + " attempt(s) left.", e.getMessage()));
		} catch (Exception e) {
			// Log the exception details

			// Return a more informative error message
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ErrorResponse("An error occurred during authentication. Please try again.", e.getMessage()));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)
			throws UserAlreadyExistsException {
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			String errorMessage = "Error: Username '" + signUpRequest.getUsername() + "' is already taken!";
			throw new UserAlreadyExistsException(errorMessage);
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			String errorMessage = "Error: Email '" + signUpRequest.getEmail() + "' is already taken!";
			throw new UserAlreadyExistsException(errorMessage);
		}

		// Create new user's account
		User user = new User();
		user.setUsername(signUpRequest.getUsername());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			List<Role> userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole.get(0));
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					List<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole.get(0));
					break;
				case "mod":
					List<Role> modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole.get(0));
					break;
				default:
					List<Role> userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole.get(0));
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}