package com.onlinetest.service;

import com.onlinetest.model.LoginRequest;
import com.onlinetest.model.User;
import com.onlinetest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Validates user login using name and roll number.
     * Returns user data map on success, or null on failure.
     */
    public Map<String, Object> login(LoginRequest request) {
        Optional<User> userOpt = userRepository
                .findByNameAndRollNumber(request.getName().trim(), request.getRollNumber().trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("name", user.getName());
            response.put("rollNumber", user.getRollNumber());
            response.put("message", "Login successful");
            return response;
        }
        return null;
    }

    /**
     * Register a new user (admin use).
     */
    public User registerUser(User user) {
        if (userRepository.existsByRollNumber(user.getRollNumber())) {
            throw new RuntimeException("Roll number already registered: " + user.getRollNumber());
        }
        return userRepository.save(user);
    }
}
