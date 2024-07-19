package com.haydt.services;


import com.haydt.dtos.requests.LoginRequestDTO;
import com.haydt.dtos.requests.RegisterRequestDTO;
import com.haydt.entities.User;


public interface AuthenticationService {
    User signup(RegisterRequestDTO input);

    User authenticate(LoginRequestDTO input);
}
