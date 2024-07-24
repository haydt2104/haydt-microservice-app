package com.haydt.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haydt.models.RedisRefreshTokenModel;

public interface RedisService {

    void saveToken(String email, String token, long expiration) throws Exception;

    public List<RedisRefreshTokenModel> getUserTokens(String email) throws Exception;

    public boolean deleteToken(String username, String token) throws JsonProcessingException;
}
