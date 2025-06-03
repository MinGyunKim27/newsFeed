package org.example.newsfeed.like.service;

import org.example.newsfeed.like.dto.LikeResponseDto;

import java.util.List;

/**
 * 좋아요(Like) 기능을 처리하는 서비스 인터페이스입니다.
 * 구현 클래스에서는 게시글에 대한 좋아요 생성, 조회, 삭제 등의 로직을 정의합니다.
 */
public interface LikeService {

    /**
     * 특정 게시글에 대해 사용자의 좋아요를 생성합니다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     */
    void createLike(Long postId, Long userId);

    /**
     * 특정 게시글을 좋아요한 사용자 목록을 조회합니다.
     *
     * @param postId 게시글 ID
     * @return 좋아요한 사용자 정보를 담은 LikeResponseDto 리스트
     */
    List<LikeResponseDto> getLikeUserList(Long postId);

    /**
     * 특정 게시글에 대해 사용자의 좋아요를 취소(삭제)합니다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     */
    void deleteLike(Long postId, Long userId);

    /**
     * 특정 게시글의 총 좋아요 수를 반환합니다.
     *
     * @param postId 게시글 ID
     * @return 좋아요 수
     */
    Long getLikeCount(Long postId);
}
