package com.haydt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haydt.dtos.requests.LoginRequestDTO;
import com.haydt.dtos.requests.RegisterRequestDTO;
import com.haydt.dtos.responses.LoginResponseDTO;
import com.haydt.entities.User;
import com.haydt.repositories.UserRepository;
import com.haydt.services.AuthenticationService;
import com.haydt.utilities.JwtUtil;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtUtil.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtUtil.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/encrypt")
    public ResponseEntity<?> getMethodName(@RequestBody LoginRequestDTO data) {
        userRepository.findByEmail(data.getEmail()).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(data.getPassword()));
            userRepository.save(user);
        });
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}