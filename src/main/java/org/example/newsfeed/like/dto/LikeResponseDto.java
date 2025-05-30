package org.example.newsfeed.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.like.entity.Like;

@Getter
@NoArgsConstructor
public class LikeResponseDto {
    private Long likeId;
    private Long userId;
    private String userName;
    private String profileImage;

    public LikeResponseDto(Like like) {
        this.likeId = like.getId();
        this.userId = like.getUser().getId();
        this.userName = like.getUser().getUsername();
        this.profileImage = like.getUser().getProfileImage();
    }
}
