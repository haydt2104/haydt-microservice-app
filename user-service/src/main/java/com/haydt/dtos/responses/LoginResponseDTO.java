package com.haydt.dtos.responses;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;

    private long expiresIn;
}
