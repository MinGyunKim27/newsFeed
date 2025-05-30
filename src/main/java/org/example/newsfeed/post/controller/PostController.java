package org.example.newsfeed.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostListResponseDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.example.newsfeed.post.service.PostService;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/posts") // api를 명시함으로 써 프론트용 API 요청이라는 것을 확실히 한다.
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody @Validated CreatePostRequestDto dto,
            HttpServletRequest request
    ) {

        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtProvider.getUserId(token);

        Long postId = postService.createPost(dto, userId);

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
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtProvider.getUserId(token);

        postService.deletePost(postId, userId);
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

    // 사람들 별로 게시물 조회
    //김민균 작성
    @GetMapping("/user/{userId}")
    public ResponseEntity<PostListResponseDto> getPostListByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<PostResponseDto> list = postService.getPostListByUser(page, size, userId);
        return ResponseEntity.ok(new PostListResponseDto(list));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody @Validated UpdatePostRequestDto dto,
            HttpServletRequest request
            ) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtProvider.getUserId(token);

        postService.updatePost(postId, dto,userId);
        return ResponseEntity.noContent().build();
    }

}


