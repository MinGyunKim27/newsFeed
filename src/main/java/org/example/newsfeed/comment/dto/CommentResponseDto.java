package org.example.newsfeed.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.comment.entity.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private String name;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.name = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
