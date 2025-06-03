package org.example.newsfeed.like.repository;

import org.example.newsfeed.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Like 엔티티에 대한 데이터 접근 레이어입니다.
 * 게시글 좋아요 관련 CRUD 및 조회 기능을 제공합니다.
 */
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * 특정 게시글(postId)에 달린 좋아요 목록을 생성일 기준 내림차순으로 조회합니다.
     *
     * @param postId 조회 대상 게시글 ID
     * @return 좋아요 리스트 (최신순 정렬)
     */
    List<Like> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * 특정 게시글(postId)의 좋아요 개수를 반환합니다.
     *
     * @param postId 집계 대상 게시글 ID
     * @return 좋아요 수
     */
    Long countByPostId(Long postId);

    /**
     * 특정 사용자(userId)가 특정 게시글(postId)에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param userId 사용자 ID
     * @param postId 게시글 ID
     * @return 해당 조건에 맞는 Like 객체 (없으면 Optional.empty)
     */
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * 특정 게시글(postId)에 달린 모든 좋아요를 삭제합니다.
     * 게시글 삭제 시 함께 호출될 수 있습니다.
     *
     * @param postId 삭제 대상 게시글 ID
     */
    void deleteAllByPost_Id(Long postId);
}
