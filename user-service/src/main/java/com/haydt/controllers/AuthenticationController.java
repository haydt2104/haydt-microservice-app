package com.haydt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.haydt.dtos.requests.GetTokenRequestDTO;
import com.haydt.dtos.requests.LoginRequestDTO;
import com.haydt.dtos.requests.RegisterRequestDTO;
import com.haydt.dtos.responses.LoginResponseDTO;
import com.haydt.entities.User;
import com.haydt.models.RedisRefreshTokenModel;
import com.haydt.repositories.UserRepository;
import com.haydt.services.AuthenticationService;
import com.haydt.services.RedisService;
import com.haydt.utilities.JwtUtil;

@BaseController
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtAccessToken = jwtUtil.generateAccessToken(authenticatedUser);

        ResponseCookie cookie = ResponseCookie.from("access_token", jwtAccessToken)
                .httpOnly(true)
                .maxAge(jwtUtil.getAccessExpirationTime() / 1000)
                .path("/")
                .sameSite("Strict")
                .secure(false)
                .build();
        try {
            redisService.saveToken(authenticatedUser.getEmail(), jwtUtil.generateRefreshToken(authenticatedUser),
                    jwtUtil.getRefreshExpirationTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setExpiresIn(jwtUtil.getAccessExpirationTime());
        loginResponse.setEmail(authenticatedUser.getEmail());
        loginResponse.setStatus(HttpStatus.OK.value());
        loginResponse.setMessage("Login successful");
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(loginResponse);
    }

    @PostMapping("/public/encrypt")
    public ResponseEntity<?> getMethodName(@RequestBody LoginRequestDTO data) {
        userRepository.findByEmail(data.getEmail()).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(data.getPassword()));
            userRepository.save(user);
        });
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("public/token")
    public ResponseEntity<?> getMethodName(@RequestBody GetTokenRequestDTO email) throws Exception {
        RedisRefreshTokenModel token = redisService.getUserTokens(email.getEmail());
        return ResponseEntity.ok(token);
    }

}