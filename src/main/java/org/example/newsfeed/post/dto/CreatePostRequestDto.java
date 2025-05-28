package org.example.newsfeed.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CreatePostRequestDto {

    // 제목
    private String title;

    // 내용
    private String content;

    // 이미지Url
    private String imageUrl;

}
