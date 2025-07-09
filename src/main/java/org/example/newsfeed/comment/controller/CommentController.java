package org.example.newsfeed.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.dto.CreateCommentRequestDto;
import org.example.newsfeed.comment.service.CommentService;
import org.example.newsfeed.global.common.dto.PagedResponse;
import org.example.newsfeed.global.util.JwtProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 댓글 생성, 조회(목록/개수), 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    /**
     * 댓글 생성 API
     * @param dto 댓글 내용 DTO
     * @param postId 댓글이 달릴 게시글 ID
     * @param userId 인증된 사용자 ID (JWT 기반 @AuthenticationPrincipal에서 추출)
     * @return 생성된 댓글 응답 DTO
     */
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @Valid @RequestBody CreateCommentRequestDto dto,
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId
    ) {
        // 댓글 생성 후 201 응답
        return new ResponseEntity<>(commentService.createComment(dto.getContent(), postId, userId), HttpStatus.CREATED);
    }

    /**
     * 댓글 목록 조회 API (페이징 처리)
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 10)
     * @param postId 대상 게시글 ID
     * @return 댓글 목록 페이징 응답
     */
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<PagedResponse<CommentResponseDto>> getCommentList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long postId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(commentService.getCommentList(pageRequest, postId), HttpStatus.OK);
    }

    /**
     * 댓글 총 개수 조회 API
     * @param postId 대상 게시글 ID
     * @return 댓글 개수(Long)
     */
    @GetMapping("/api/posts/{postId}/comments/count")
    public ResponseEntity<Long> getCommentList(
            @PathVariable Long postId
    ) {
        return new ResponseEntity<>(commentService.getCommentTotalCount(postId), HttpStatus.OK);
    }

    /**
     * 댓글 수정 API
     * @param dto 수정할 댓글 내용 DTO
     * @param commentId 수정할 댓글 ID
     * @param userId 인증된 사용자 ID (JWT 기반 @AuthenticationPrincipal에서 추출)
     * @return 수정된 댓글 응답 DTO
     */
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @Valid @RequestBody CreateCommentRequestDto dto,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        return new ResponseEntity<>(commentService.updateComment(dto.getContent(), commentId, userId), HttpStatus.OK);
    }

    /**
     * 댓글 삭제 API
     * @param commentId 삭제할 댓글 ID
     * @param userId 인증된 사용자 ID (JWT 기반 @AuthenticationPrincipal에서 추출)
     * @return HTTP 200 OK (본문 없음)
     */
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId) {

        commentService.deleteComment(commentId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
