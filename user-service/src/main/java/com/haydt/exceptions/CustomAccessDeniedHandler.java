package com.haydt.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("status", String.valueOf(HttpStatus.FORBIDDEN.value()));
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", accessDeniedException.getMessage());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", String.valueOf(System.currentTimeMillis()));
        errorDetails.put("description", "You are not authorized to access this resource");
        errorDetails.put("method", request.getMethod());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
