package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.newsfeed.post.entitiy.Post;

@AllArgsConstructor
@Builder
@Getter
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private String author;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.author = post.getUser().getUsername();
    }
}
