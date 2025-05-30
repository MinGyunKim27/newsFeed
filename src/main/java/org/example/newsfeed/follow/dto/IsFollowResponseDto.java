package org.example.newsfeed.follow.dto;

import lombok.Getter;

@Getter
public class IsFollowResponseDto {
    private final boolean isFollow;

    public IsFollowResponseDto(boolean isFollow){
        this.isFollow = isFollow;
    }
}
