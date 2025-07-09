package org.example.newsfeed.post.repository;

import org.example.newsfeed.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글(Post) 엔티티에 대한 데이터베이스 접근을 담당하는 리포지토리 인터페이스입니다.
 * JpaRepository를 확장하여 기본적인 CRUD 및 페이징 기능을 제공합니다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 특정 사용자가 작성한 게시글을 페이징 처리하여 조회하는 메서드입니다.
     *
     * @param userId   게시글 작성자의 사용자 ID
     * @param pageable 페이징 정보를 담고 있는 객체 (페이지 번호, 크기, 정렬 등)
     * @return 해당 사용자의 게시글을 Page 객체로 반환
     */
    Page<Post> findAllByUser_Id(Long userId, Pageable pageable);
}
