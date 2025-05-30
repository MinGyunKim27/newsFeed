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

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final JwtProvider jwtProvider;

    // 생성 C
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> createLike(@PathVariable Long postId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);
        likeService.createLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 조회 R
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<List<LikeResponseDto>> getLikeUserList(@PathVariable Long postId) {
        return new ResponseEntity<>(likeService.getLikeUserList(postId), HttpStatus.OK);
    }

    @GetMapping("/api/posts/{postId}/likes/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        return new ResponseEntity<>(likeService.getLikeCount(postId), HttpStatus.OK);
    }

    // 삭제 D
    @DeleteMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> deleteLike(@PathVariable Long postId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        likeService.deleteLike(postId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
