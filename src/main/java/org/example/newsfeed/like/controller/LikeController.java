package org.example.newsfeed.like.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 좋아요 기능을 담당하는 컨트롤러입니다.
 * 좋아요 생성, 조회, 삭제 기능을 제공합니다.
 */
@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final JwtProvider jwtProvider;

    /**
     * 좋아요 생성 (사용자가 게시글에 좋아요를 누름)
     *
     * @param postId 좋아요 대상 게시글 ID
     * @param request 인증 토큰이 포함된 HTTP 요청
     * @return HTTP 201 Created 응답
     */
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> createLike(@PathVariable Long postId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        likeService.createLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 게시글을 좋아요한 사용자 목록 조회
     *
     * @param postId 게시글 ID
     * @return 좋아요한 사용자 정보 리스트
     */
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<List<LikeResponseDto>> getLikeUserList(@PathVariable Long postId) {
        return new ResponseEntity<>(likeService.getLikeUserList(postId), HttpStatus.OK);
    }

    /**
     * 게시글 좋아요 개수 조회
     *
     * @param postId 게시글 ID
     * @return 좋아요 수 (Long)
     */
    @GetMapping("/api/posts/{postId}/likes/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        return new ResponseEntity<>(likeService.getLikeCount(postId), HttpStatus.OK);
    }

    /**
     * 좋아요 취소 (사용자가 게시글 좋아요를 취소함)
     *
     * @param postId 게시글 ID
     * @param request 인증 토큰이 포함된 HTTP 요청
     * @return HTTP 200 OK 응답
     */
    @DeleteMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> deleteLike(@PathVariable Long postId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        likeService.deleteLike(postId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
