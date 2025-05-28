package org.example.newsfeed.post.service;

import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;

public interface PostService {

    // 포스트 생성
    Long createPost(CreatePostRequestDto dto, User user);

    PostResponseDto findById(Long postId);

    void deletePost(Long postId, User user);
}
