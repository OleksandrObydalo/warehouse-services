package org.example.webclient.dto;

public class UserDTO {
    private String userId;
    private String username;
    private String password;

    public UserDTO() {
    }

    public UserDTO(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

