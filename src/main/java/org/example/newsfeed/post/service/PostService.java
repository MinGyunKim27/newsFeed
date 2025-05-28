package org.example.newsfeed.post.service;

import org.example.newsfeed.post.dto.CreatePostRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.UpdatePostRequestDto;
import org.springframework.data.domain.Page;

public interface PostService {

    // 포스트 생성
    Long createPost(CreatePostRequestDto dto, User user);

    PostResponseDto findById(Long postId);

    void deletePost(Long postId, User user);

    Page<PostResponseDto> getPostList(int page, int size);

    void updatePost(Long postId, UpdatePostRequestDto dto, User user);
}
