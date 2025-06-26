package com.example.demo.user.dto;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private Integer argent;
    private Boolean isActive;
    
    // Constructeurs
    public UserDto() {}
    
    public UserDto(Long id, String username, String email, LocalDateTime createdAt, Integer argent, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.argent = argent;
        this.isActive = isActive;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getArgent() { return argent; }
    public void setArgent(Integer argent) { this.argent = argent; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
