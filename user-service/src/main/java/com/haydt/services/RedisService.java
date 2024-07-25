package com.haydt.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.haydt.models.RedisRefreshTokenModel;

public interface RedisService {

    void saveToken(String email, String token, long expiration) throws Exception;

    public RedisRefreshTokenModel getUserTokens(String email) throws Exception;

    public boolean deleteToken(String username, String token) throws JsonProcessingException;
}
