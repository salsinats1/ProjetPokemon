package com.example.demo.user.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.User;

@Component
public class UserMapper {
    
    public UserDto toDto(User user) {
        if (user == null) return null;
        
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getArgent(),
            user.getIsActive()
        );
    }
    
    public User toEntity(UserDto userDto) {
        if (userDto == null) return null;
        
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setArgent(userDto.getArgent());
        user.setIsActive(userDto.getIsActive());
        
        return user;
    }
    
    public User createEntityFromDto(String username, String email, String password) {
        return new User(username, email, password);
    }
}
