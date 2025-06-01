package org.example.newsfeed.comment.repository;

import org.example.newsfeed.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    Long countByPostId(Long post_id);

    List<Comment> findByPost_Id(Long postId);

    void deleteAllByPost_Id(Long postId);
}
