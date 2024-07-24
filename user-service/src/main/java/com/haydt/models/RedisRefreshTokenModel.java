package com.haydt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisRefreshTokenModel {
    private String token;
    private long expiresIn;
    private String createdAt;
}
