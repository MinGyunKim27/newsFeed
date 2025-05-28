package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private String author;

}
