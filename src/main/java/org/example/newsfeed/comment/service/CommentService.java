package org.example.newsfeed.comment.service;

import jakarta.validation.constraints.NotBlank;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.global.common.dto.PagedResponse;
import org.springframework.data.domain.PageRequest;

/**
 * 댓글 기능에 대한 서비스 인터페이스입니다.
 * 댓글의 생성, 조회, 수정, 삭제 등 핵심 비즈니스 로직 정의.
 */
public interface CommentService {

    /**
     * 댓글을 생성합니다.
     *
     * @param content 댓글 내용 (빈 문자열 허용 안 됨)
     * @param postId 댓글이 달릴 게시글 ID
     * @param userId 댓글을 작성하는 사용자 ID
     * @return 생성된 댓글의 응답 DTO
     */
    CommentResponseDto createComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long postId, Long userId);

    /**
     * 게시글에 달린 댓글 목록을 페이징 처리하여 조회합니다.
     *
     * @param pageRequest 페이지 정보 (페이지 번호, 크기 등)
     * @param postId 대상 게시글 ID
     * @return 페이징된 댓글 응답 DTO 리스트
     */
    PagedResponse<CommentResponseDto> getCommentList(PageRequest pageRequest, Long postId);

    /**
     * 댓글 내용을 수정합니다.
     *
     * @param content 수정할 댓글 내용 (빈 문자열 허용 안 됨)
     * @param commentId 수정 대상 댓글 ID
     * @param userId 댓글 작성자 본인 여부 확인용 사용자 ID
     * @return 수정된 댓글의 응답 DTO
     */
    CommentResponseDto updateComment(@NotBlank(message = "내용은 필수 입력값입니다") String content, Long commentId, Long userId);

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 삭제 대상 댓글 ID
     * @param userId 댓글 작성자 본인 여부 확인용 사용자 ID
     */
    void deleteComment(Long commentId, Long userId);

    /**
     * 게시글에 달린 전체 댓글 수를 조회합니다.
     *
     * @param postId 대상 게시글 ID
     * @return 댓글 총 개수
     */
    Long getCommentTotalCount(Long postId);
}
