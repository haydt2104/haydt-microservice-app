package com.haydt.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", authException.getMessage());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", String.valueOf(System.currentTimeMillis()));
        errorDetails.put("description", "You are not authorized to access this resource");
        errorDetails.put("method", request.getMethod());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
