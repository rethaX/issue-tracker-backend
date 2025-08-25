package com.issuetracker.issue_tracker_backend.service;

import com.issuetracker.issue_tracker_backend.entity.User;
import com.issuetracker.issue_tracker_backend.repository.UserRepository;
import com.issuetracker.issue_tracker_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${stack.auth.project.id}")
    private String projectId;

    private final RestTemplate restTemplate = new RestTemplate();

    public User getOrCreateUser(String token) {
        String email = jwtUtil.extractUsername(token);
        if (!jwtUtil.validateToken(token, email)) {
            throw new RuntimeException("Invalid token");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // Fetch user info from Neon Auth API
        String userInfoUrl = "https://api.stack-auth.com/api/v1/projects/" + projectId + "/users/me";
        Map<String, Object> userInfo = restTemplate.getForObject(
                userInfoUrl,
                Map.class,
                Map.of("Authorization", "Bearer " + token)
        );

        if (userInfo == null || !userInfo.containsKey("email")) {
            throw new RuntimeException("Failed to fetch user info");
        }

        User user = new User();
        user.setEmail((String) userInfo.get("email"));
        user.setPasswordHash("neon-auth-managed"); // No password needed
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOpt.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), new ArrayList<>());
    }
}