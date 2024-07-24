package com.haydt.services.implement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        // Lấy danh sách token hiện tại từ Redis
        String tokensJson = (String) valueOps.get(email);

        System.out.println("List token save: " + tokensJson);

        List<RedisRefreshTokenModel> tokens;
        if (tokensJson != null) {
            // Nếu đã có dữ liệu, chuyển đổi từ JSON sang List
            tokens = objectMapper.readValue(tokensJson, new TypeReference<List<RedisRefreshTokenModel>>() {
            });
        } else {
            // Nếu chưa có, tạo danh sách mới
            tokens = new ArrayList<>();
        }

        // Thêm token mới vào danh sách
        tokens.add(new RedisRefreshTokenModel(token, expiration,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

        // Chuyển đổi danh sách sang JSON
        String updatedTokensJson = objectMapper.writeValueAsString(tokens);

        // Lưu danh sách token cập nhật vào Redis
        valueOps.set(email, updatedTokensJson);

    }

    @Override
    // Lấy tất cả token của user
    public List<RedisRefreshTokenModel> getUserTokens(String email) throws Exception {
        // Lấy danh sách token hiện tại từ Redis
        String tokensJson = (String) valueOps.get(email);

        System.out.println("List token: " + tokensJson);

        if (tokensJson == null) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không có dữ liệu
        }

        // Chuyển đổi từ JSON sang List
        return objectMapper.readValue(tokensJson, new TypeReference<List<RedisRefreshTokenModel>>() {
        });
    }

    @Override
    public boolean deleteToken(String username, String token) throws JsonProcessingException {
        // Lấy danh sách token hiện tại từ Redis
        String tokensJson = (String) valueOps.get(username);

        if (tokensJson == null) {
            return false; // Không có token để xóa
        }

        // Chuyển đổi từ JSON sang List
        List<RedisRefreshTokenModel> tokens = objectMapper.readValue(tokensJson,
                new TypeReference<List<RedisRefreshTokenModel>>() {
                });

        // Xóa token nếu tồn tại
        boolean removed = tokens.removeIf(t -> t.getToken().equals(token));

        if (removed) {
            // Cập nhật lại danh sách nếu token bị xóa
            String updatedTokensJson = objectMapper.writeValueAsString(tokens);
            valueOps.set(username, updatedTokensJson);
        }

        return removed;
    }

}
