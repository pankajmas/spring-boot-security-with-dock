package com.example.demo.controller;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.ErrorResponse;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.utility.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthUtil authUtil;

    public AuthController(UserRepository userRepository,
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
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                loginRequest.getPassword()));

                authUtil.resetLoginAttempts(loginRequest.getUsername());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String jwt = jwtUtils.generateJwtToken(authentication);
                return ResponseEntity.ok(new JwtResponse(jwt
                        , userDetails.getUsername(), getRoles(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                        "Maximum login attempts reached. User is temporarily locked out for 20 minutes.", null));
            }
        } catch (AuthenticationException e) {
            authUtil.handleAuthenticationFailure(loginRequest.getUsername());
            int remainingAttempts = authUtil.getRemainingLoginAttempts(loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                    "Authentication failed. " + remainingAttempts + " attempt(s) left.", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("An error occurred during authentication. Please try again.", e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
            }

            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = new HashSet<>();
            signUpRequest.getRole().forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(getRole(ERole.ROLE_ADMIN));
                        break;
                    case "mod":
                        roles.add(getRole(ERole.ROLE_MODERATOR));
                        break;
                    default:
                        roles.add(getRole(ERole.ROLE_USER));
                        break;
                }
            });
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("An error occurred during registration. Please try again.", e.getMessage()));
        }
    }

    private Set<String> getRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private Role getRole(ERole role) {
        return (Role) roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
    }
}
