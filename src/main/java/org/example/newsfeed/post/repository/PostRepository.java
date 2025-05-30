package org.example.newsfeed.post.repository;

import org.example.newsfeed.post.entitiy.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser_Id(Long userId, Pageable pageable);

    // 기본 CRUD method
}
