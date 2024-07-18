package com.haydt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haydt.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("Success");
        return ResponseEntity.ok("Hello hello");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser() {
        System.out.println("Success get user");
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
