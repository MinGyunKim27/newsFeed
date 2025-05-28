package org.example.newsfeed.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody CreatePostRequestDto dto,
            HttpServletRequest request
            ) {

        // 필터에서 JWT 검증하고 인증된 User 객체를 직접 request에 넣어준다.
        User user =(User) request.getAttribute("user");

        Long postId = postService.createPost(dto, user);

        return ResponseEntity.ok(postId);
    }



}
