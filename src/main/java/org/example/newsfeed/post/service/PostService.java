package org.example.newsfeed.post.service;

import org.example.newsfeed.post.dto.CreatePostRequestDto;

public interface PostService {

    // 포스트 생성
    Long createPost(CreatePostRequestDto dto, User user);
}
