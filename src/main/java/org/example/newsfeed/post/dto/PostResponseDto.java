package org.example.newsfeed.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.newsfeed.image.entity.Image;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.image.dto.ImageUploadResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private List<ImageUploadResponseDto> images;

    private String author;

    private String authorImageUrl;

    private Long authorId;

    private LocalDateTime createdAt;

    public PostResponseDto(Post post,List<ImageUploadResponseDto> dto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.images = dto;
        this.author = post.getUser().getUsername();
        this.authorImageUrl = post.getUser().getProfileImage();
        this.authorId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
    }
}
