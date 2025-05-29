package org.example.newsfeed.follow.dto;

import lombok.Getter;
import org.example.newsfeed.user.entity.User;

@Getter
public class FollowResponseDto {
    private Long userId;
    private String username;

    public FollowResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }
}
