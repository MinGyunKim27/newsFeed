package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostListResponseDto {

    private List<PostResponseDto> posts;

    private int totalPages;

    private int currnetPage;

    public PostListResponseDto(Page<PostResponseDto> page) {
        this.posts = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currnetPage = page.getNumber();
    }
}

