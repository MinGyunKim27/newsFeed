package org.example.newsfeed.comment.repository;

import org.example.newsfeed.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment 엔티티에 대한 데이터베이스 접근을 처리하는 JPA Repository 인터페이스입니다.
 * 기본적인 CRUD 기능은 JpaRepository에서 상속받으며,
 * 게시글(Post)에 따른 댓글 조회, 삭제 등의 커스텀 메서드를 제공합니다.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글의 모든 댓글을 최신순으로 페이징하여 조회합니다.
     *
     * @param postId   게시글 ID
     * @param pageable 페이징 정보 (페이지 번호, 크기 등)
     * @return Page<Comment> 페이징된 댓글 목록
     */
    Page<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    /**
     * 특정 게시글에 달린 댓글의 개수를 반환합니다.
     *
     * @param post_id 게시글 ID
     * @return 댓글 수
     */
    Long countByPostId(Long post_id);

    /**
     * 특정 게시글에 달린 모든 댓글을 리스트 형태로 조회합니다.
     *
     * @param postId 게시글 ID
     * @return 댓글 리스트
     */
    List<Comment> findByPost_Id(Long postId);

    /**
     * 특정 게시글에 달린 모든 댓글을 삭제합니다.
     *
     * @param postId 게시글 ID
     */
    void deleteAllByPost_Id(Long postId);
}
