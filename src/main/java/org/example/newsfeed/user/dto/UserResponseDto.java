package org.example.newsfeed.user.dto;

import org.example.newsfeed.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long userId;
    private String username;
    private String email;
    private String profileImage;
    private String bio;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.bio = user.getBio();
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user);
    }
}
