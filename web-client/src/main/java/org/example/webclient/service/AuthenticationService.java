package org.example.webclient.service;

import org.example.webclient.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public UserDTO authenticate(String username, String password) {
        for (UserDTO user : users.values()) {
            if (user.getUsername().equals(username) && user.getUserId().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public UserDTO getUserById(String userId) {
        return users.get(userId);
    }

    public boolean registerUser(String userId, String username, String password) {
        if (users.containsKey(userId)) {
            return false; // User already exists
        }
        users.put(userId, new UserDTO(userId, username));
        return true;
    }
}

