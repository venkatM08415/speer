package com.app.speer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.app.speer.configuration.JwtUtils;
import com.app.speer.exceptions.ResourceNotFoundException;
import com.app.speer.model.Users;
import com.app.speer.repository.UserRepository;
import com.app.speer.request.LoginRequest;
import com.app.speer.request.SignupRequest;
import com.app.speer.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for successful user registration
    @Test
    public void testRegisterUser_Success() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");


        ResponseEntity<?> response = authController.registerUser(signupRequest);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    // Test for user registration with an existing username
    @Test
    public void testRegisterUser_UsernameTaken() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existinguser");
        signupRequest.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);


        ResponseEntity<?> response = authController.registerUser(signupRequest);


        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username already taken", response.getBody());
        verify(userRepository, never()).save(any(Users.class));
    }

    // Test for successful user login
    @Test
    public void testAuthenticateUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateJwtToken("testuser")).thenReturn("jwtToken");


        ResponseEntity<?> response = authController.authenticateUser(loginRequest);


        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals("testuser", jwtResponse.getUsername());
    }

    // Test for user login with invalid credentials
    @Test
    public void testAuthenticateUser_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);


        assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }

    // Test for user login with a non-existent username
    @Test
    public void testAuthenticateUser_UserNotFound() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password123");

        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }
}
