package org.example.newsfeed.like.repository;

import org.example.newsfeed.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    Long countByPostId(Long postId);
}
