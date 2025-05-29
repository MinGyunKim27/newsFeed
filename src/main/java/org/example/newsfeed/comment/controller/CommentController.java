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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    // 생성 C
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @Valid @RequestBody CreateCommentRequestDto dto,
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        return new ResponseEntity<>(commentService.createComment(dto.getContent(), postId, userId), HttpStatus.CREATED);
    }

    // 조회 R
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<PagedResponse<CommentResponseDto>> getCommentList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long postId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return new ResponseEntity<>(commentService.getCommentList(pageRequest, postId), HttpStatus.OK);
    }

    // 수정 U
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @Valid @RequestBody CreateCommentRequestDto dto,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        return new ResponseEntity<>(commentService.updateComment(dto.getContent(), commentId, userId), HttpStatus.OK);
    }

    // 삭제 D
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        commentService.deleteComment(commentId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
