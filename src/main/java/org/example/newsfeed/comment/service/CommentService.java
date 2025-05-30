package org.example.newsfeed.comment.service;

import jakarta.validation.constraints.NotBlank;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.global.common.dto.PagedResponse;
import org.springframework.data.domain.PageRequest;

public interface CommentService {
    CommentResponseDto createComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long postId, Long userId);

    PagedResponse<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId);

    CommentResponseDto updateComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);

    Long getCommentTotalCount(Long postId);
}
