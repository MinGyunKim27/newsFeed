package org.example.newsfeed.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.like.entity.Like;

@Getter
@NoArgsConstructor
public class LikeResponseDto {
    private String name;

    public LikeResponseDto(Like like) {
        // todo: 추후 유저 맵핑을 통해 이름 할당 임시값 생성일
        this.name = like.getCreatedAt().toString();
    }
}
