package com.haydt.services.implement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haydt.models.RedisRefreshTokenModel;
import com.haydt.services.RedisService;

@Service
public class RedisServiceImplement implements RedisService {

    private final ValueOperations<String, Object> valueOps;
    private final ObjectMapper objectMapper;

    public RedisServiceImplement(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.valueOps = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveToken(String email, String token, long expiration) throws Exception {

        // Tạo một đối tượng RedisRefreshTokenModel với token và thời gian hết hạn
        RedisRefreshTokenModel refreshToken = new RedisRefreshTokenModel(
                token,
                expiration,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));

        // Chuyển đổi đối tượng sang JSON
        String tokenJson = objectMapper.writeValueAsString(refreshToken);

        // Lưu đối tượng token dưới dạng JSON vào Redis
        valueOps.set(email, tokenJson);

    }

    @Override
    public RedisRefreshTokenModel getUserTokens(String email) throws Exception {
        String tokensJson = (String) valueOps.get(email);

        if (tokensJson == null) {
            return null;
        }

        // Chuyển đổi từ JSON sang List
        return objectMapper.readValue(tokensJson, new TypeReference<RedisRefreshTokenModel>() {
        });
    }

    @Override
    public boolean deleteToken(String email, String token) throws JsonProcessingException {
        // Lấy token JSON hiện tại từ Redis
        String tokenJson = (String) valueOps.get(email);

        if (tokenJson == null) {
            return false;
        }

        // Chuyển đổi từ JSON sang đối tượng RedisRefreshTokenModel
        RedisRefreshTokenModel storedToken = objectMapper.readValue(tokenJson, RedisRefreshTokenModel.class);

        // Kiểm tra token có khớp không
        if (storedToken.getToken().equals(token)) {
            // Nếu khớp, xóa token
            valueOps.getOperations().delete(email);
            return true;
        }

        return false;
    }

}
