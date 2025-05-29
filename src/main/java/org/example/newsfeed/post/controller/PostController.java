package org.example.newsfeed.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostListResponseDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.example.newsfeed.post.service.PostService;
import org.example.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

        // 반환할 URI 생성
        URI location = URI.create("/api/posts/" + postId);

        return ResponseEntity.created(location).body(postId);
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

    // 쿼리 스트링을 통해 페이지 정보를 전달 받자
    // 기본 0번 페이지부터 5개씩
    @GetMapping
    public ResponseEntity<PostListResponseDto> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<PostResponseDto> list = postService.getPostList(page, size);
        return ResponseEntity.ok(new PostListResponseDto(list));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto dto,
            HttpServletRequest request
            ) {
        User user = (User) request.getAttribute("user");
        postService.updatePost(postId, dto,user);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }
}


