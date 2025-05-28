package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostListResponseDto {

    private List<PostResponseDto> posts;

    private int totalPages;

    private int currnetPage;
}
