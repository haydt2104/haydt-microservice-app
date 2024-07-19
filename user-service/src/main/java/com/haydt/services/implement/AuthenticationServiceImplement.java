package com.haydt.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.haydt.dtos.requests.LoginRequestDTO;
import com.haydt.dtos.requests.RegisterRequestDTO;
import com.haydt.entities.User;
import com.haydt.repositories.UserRepository;
import com.haydt.services.AuthenticationService;

@Service
public class AuthenticationServiceImplement implements AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public User signup(RegisterRequestDTO input) {
        User user = new User();
        user.setEmail(input.getEmail());
        user.setFullname(input.getFullName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginRequestDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

}
