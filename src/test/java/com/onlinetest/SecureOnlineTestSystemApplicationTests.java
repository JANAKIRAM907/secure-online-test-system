package com.onlinetest;

import com.onlinetest.model.User;
import com.onlinetest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecureOnlineTestSystemApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Verifies Spring context starts correctly
    }

    @Test
    void testUserNotFoundForInvalidRollNumber() {
        Optional<User> user = userRepository.findByRollNumber("INVALID999");
        assertFalse(user.isPresent(), "Should return empty for invalid roll number");
    }
}
