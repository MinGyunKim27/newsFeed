package org.example.newsfeed.like.repository;

import org.example.newsfeed.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
