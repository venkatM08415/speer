package com.app.speer.controller;


import com.app.speer.configuration.JwtUtils;
import com.app.speer.response.JwtResponse;
import com.app.speer.request.LoginRequest;
import com.app.speer.request.SignupRequest;
import com.app.speer.model.Users;
import com.app.speer.repository.UserRepository;
import com.app.speer.exceptions.ResourceNotFoundException;
import com.app.speer.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        logger.info("User SignupRequest with username : {}", signupRequest.getUsername());

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            logger.info("Username already taken : {}", signupRequest.getUsername());
            return ResponseEntity.badRequest().body("Username already taken");
        }
        Users users = new Users();
        users.setUsername(signupRequest.getUsername());
        users.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(users);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("User LoginRequest with username : {}", loginRequest.getUsername());
        Users users = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), users.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String jwt = jwtUtils.generateJwtToken(users.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt, users.getId(), users.getUsername()));
    }
}

