package org.example.newsfeed.image.repository;

import org.example.newsfeed.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Image 엔티티에 대한 데이터 접근 레이어입니다.
 * Spring Data JPA를 사용하여 기본 CRUD 및 사용자 정의 메서드를 제공합니다.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * 특정 게시글(postId)에 속한 모든 이미지 목록을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 해당 게시글에 연결된 이미지 리스트
     */
    List<Image> findAllByPost_Id(Long postId);
}
