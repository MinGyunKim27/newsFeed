package org.example.newsfeed.user.dto;

import lombok.Getter;
import org.example.newsfeed.user.entity.User;

@Getter
public class UserResponseDto {
    private Long userId;

    private String username;

    private String email;

    public void toDto(User user){
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public UserResponseDto(User user){
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
