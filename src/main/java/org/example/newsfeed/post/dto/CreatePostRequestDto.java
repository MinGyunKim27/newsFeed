package org.example.newsfeed.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequestDto {

    // 제목
    private String title;

    // 내용
    private String content;

    // 이미지Url
    private String imageUrl;

}
