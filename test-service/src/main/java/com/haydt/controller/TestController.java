package com.haydt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("Success");
        return ResponseEntity.ok("Hello");
    }

}
