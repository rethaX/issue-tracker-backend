package com.issuetracker.issue_tracker_backend.controller;

import com.issuetracker.issue_tracker_backend.entity.User;
import com.issuetracker.issue_tracker_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<User> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        User user = userService.getOrCreateUser(token);
        return ResponseEntity.ok(user);
    }
}