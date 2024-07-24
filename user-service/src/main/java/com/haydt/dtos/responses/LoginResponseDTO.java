package com.haydt.dtos.responses;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private int status;

    private String email;

    private String message;

    private long expiresIn;
}
