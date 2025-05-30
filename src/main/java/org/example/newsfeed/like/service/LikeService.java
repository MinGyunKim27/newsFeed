package org.example.newsfeed.like.service;

import org.example.newsfeed.like.dto.LikeResponseDto;

import java.util.List;

public interface LikeService {
    void createLike(Long postId, Long userId);

    List<LikeResponseDto> getLikeUserList(Long postId);

    void deleteLike(Long postId, Long userId);

    Long getLikeCount(Long postId);
}
