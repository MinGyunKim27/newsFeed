package org.example.newsfeed.comment.service;

import jakarta.validation.constraints.NotBlank;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CommentService {
    CommentResponseDto createComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long postId);

    Page<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId);

    CommentResponseDto updateComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long commentId);

    void deletePlan(Long commentId);
}
