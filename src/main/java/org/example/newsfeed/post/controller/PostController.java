package org.example.newsfeed.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts") // api를 명시함으로 써 프론트용 API 요청이라는 것을 확실히 한다.
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody CreatePostRequestDto dto,
            HttpServletRequest request
    ) {

        // 필터에서 JWT 검증하고 User를 꺼내서 넣은 User 객체를 직접 request에서 꺼내서 사용한다.
        User user = (User) request.getAttribute("user");

        Long postId = postService.createPost(dto, user);

        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto dto = postService.findById(postId);
        return ResponseEntity.ok(dto);
    }



    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");

        postService.deletePost(postId, user);
        return ResponseEntity.noContent().build();
    }

}


