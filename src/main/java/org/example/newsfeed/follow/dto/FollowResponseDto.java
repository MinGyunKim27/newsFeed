package org.example.newsfeed.follow.dto;

import lombok.Getter;
import org.example.newsfeed.user.entity.User;

@Getter
public class FollowResponseDto {
    private Long userId;
    private String username;
    private Long followsCount;

    // 기존 생성자 (하위 호환성 유지)
    public FollowResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.followsCount = 0L;  // 기본값
    }

    // followsCount를 포함한 생성자
    public FollowResponseDto(User user, Long followsCount) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.followsCount = followsCount;
    }

}
