package org.example.newsfeed.post.service;

import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.springframework.data.domain.Page;

/**
 * 게시글(Post) 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * 게시글 생성, 조회, 수정, 삭제 및 페이징 처리를 포함합니다.
 */
public interface PostService {

    /**
     * 게시글을 생성합니다.
     *
     * @param dto 게시글 생성 요청 DTO
     * @param userId 작성자 사용자 ID
     * @return 생성된 게시글의 ID
     */
    Long createPost(CreatePostRequestDto dto, Long userId);

    /**
     * 게시글 ID로 게시글을 조회합니다.
     *
     * @param postId 조회할 게시글 ID
     * @return 게시글 응답 DTO
     */
    PostResponseDto findById(Long postId);

    /**
     * 게시글을 삭제합니다.
     *
     * @param postId 삭제할 게시글 ID
     * @param userId 삭제 요청 사용자 ID (권한 검증용)
     */
    void deletePost(Long postId, Long userId);

    /**
     * 전체 게시글 목록을 페이징하여 조회합니다.
     *
     * @param page 요청 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 표시할 게시글 수
     * @return 게시글 응답 DTO 페이지 객체
     */
    Page<PostResponseDto> getPostList(int page, int size);

    /**
     * 특정 사용자가 작성한 게시글 목록을 페이징하여 정렬 기준에 따라 조회합니다.
     *
     * @param page 요청 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 표시할 게시글 수
     * @param userId 사용자 ID
     * @param sort 정렬 기준 (예: "createdAt", "likeCount" 등)
     * @return 게시글 응답 DTO 페이지 객체
     */
    Page<PostResponseDto> getPostListByUser(int page, int size, Long userId, String sort);

    /**
     * 게시글을 수정합니다.
     *
     * @param postId 수정 대상 게시글 ID
     * @param dto 게시글 수정 요청 DTO
     * @param userId 수정 요청 사용자 ID (권한 검증용)
     */
    void updatePost(Long postId, UpdatePostRequestDto dto, Long userId);
}
