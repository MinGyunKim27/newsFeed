package org.example.newsfeed.like.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 생성 C
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> createLike(@PathVariable Long postId) {

        return new ResponseEntity<>(likeService.createLike(postId), HttpStatus.CREATED);
    }

    // 조회 R
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<List<LikeResponseDto>> getLikeList(@PathVariable Long postId) {
        return new ResponseEntity<>(likeService.getLikeList(postId), HttpStatus.OK);
    }

    // 삭제 D
    @DeleteMapping("/api/likes/{likeId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long likeId) {
        likeService.deleteLike(likeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
