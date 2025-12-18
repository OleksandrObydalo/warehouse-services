package org.example.webclient.service;

import org.example.webclient.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory authentication service.
 * In production, this should connect to a user database or authentication service.
 */
@Service
public class AuthenticationService {

    // In-memory user storage (for demonstration)
    // In production, this should be a database
    private final Map<String, UserDTO> users = new HashMap<>();

    public AuthenticationService() {
        // Pre-populate with some test users
        users.put("u001", new UserDTO("u001", "user1"));
        users.put("u002", new UserDTO("u002", "user2"));
        users.put("u003", new UserDTO("u003", "user3"));
        users.put("admin", new UserDTO("admin", "admin"));
    }

    /**
     * Authenticate user with username and password.
     * For simplicity, password is the same as userId.
     * In production, use proper password hashing (BCrypt, etc.)
     */
    public UserDTO authenticate(String username, String password) {
        for (UserDTO user : users.values()) {
            if (user.getUsername().equals(username) && user.getUserId().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get user by userId.
     */
    public UserDTO getUserById(String userId) {
        return users.get(userId);
    }

    /**
     * Register a new user.
     */
    public boolean registerUser(String userId, String username, String password) {
        if (users.containsKey(userId)) {
            return false; // User already exists
        }
        users.put(userId, new UserDTO(userId, username));
        return true;
    }
}

