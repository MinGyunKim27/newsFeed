package org.example.newsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;
}
