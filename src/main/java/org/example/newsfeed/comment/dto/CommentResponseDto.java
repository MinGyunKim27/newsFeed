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
        // todo: 추후 작성자 이름 매칭
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
