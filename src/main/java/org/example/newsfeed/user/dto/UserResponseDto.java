package org.example.newsfeed.user.dto;

import lombok.Setter;
import org.example.newsfeed.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String username;
    private final String email;
    private final String profileImage;
    private final String bio;
    @Setter
    private Long followerCount;

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
