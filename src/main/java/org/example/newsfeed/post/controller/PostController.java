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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 게시글 관련 HTTP 요청을 처리하는 컨트롤러 클래스
 * RESTful API 형식으로 게시글 생성, 조회, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/posts") // API 명시로 클라이언트 용도 URL을 명확히 구분
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    /**
     * 게시글 생성
     * @param dto 게시글 생성 요청 데이터
     * @param request HTTP 요청 (Authorization 헤더에서 JWT 추출)
     * @return 생성된 게시글의 ID와 Location 헤더 포함
     */
    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody @Validated CreatePostRequestDto dto,
            HttpServletRequest request
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtProvider.getUserId(token);

        // 게시글 생성
        Long postId = postService.createPost(dto, userId);

        // 응답 Location 헤더 생성
        URI location = URI.create("/api/posts/" + postId);

        return ResponseEntity.created(location).body(postId);
    }

    /**
     * 게시글 단건 조회
     * @param postId 게시글 ID
     * @return 게시글 상세 DTO
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto dto = postService.findById(postId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 ID
     * @param request Authorization 헤더에서 사용자 인증
     * @return 204 No Content
     */
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

    /**
     * 게시글 목록 조회 (페이지네이션)
     * 기본값: page=0, size=5
     *
     * @param page 페이지 번호
     * @param size 페이지 당 게시글 수
     * @return 게시글 리스트 DTO
     */
    @GetMapping
    public ResponseEntity<PostListResponseDto> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<PostResponseDto> list = postService.getPostList(page, size);
        return ResponseEntity.ok(new PostListResponseDto(list));
    }

    /**
     * 특정 유저의 게시글 목록 조회
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sort 정렬 방식 ("latest", "oldest" 등)
     * @return 해당 사용자의 게시글 리스트
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PostListResponseDto> getPostListByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "latest", required = false) String sort
    ) {
        Page<PostResponseDto> list = postService.getPostListByUser(page, size, userId, sort);
        return ResponseEntity.ok(new PostListResponseDto(list));
    }

    /**
     * 게시글 수정
     * @param postId 수정할 게시글 ID
     * @param dto 수정 요청 데이터
     * @param request Authorization 헤더에서 사용자 인증
     * @return 204 No Content
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody @Validated UpdatePostRequestDto dto,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtProvider.getUserId(token);

        postService.updatePost(postId, dto, userId);
        return ResponseEntity.noContent().build();
    }
}
