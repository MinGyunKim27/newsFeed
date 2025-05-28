package org.example.newsfeed.post.dto;

import lombok.Getter;

@Getter
public class UpdatePostRequestDto {

    private String title;
    private String Content;
    private String imageUrl;
}
