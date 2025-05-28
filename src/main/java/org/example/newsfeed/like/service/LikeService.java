package org.example.newsfeed.like.service;

import org.example.newsfeed.like.dto.LikeResponseDto;

import java.util.List;

public interface LikeService {
    LikeResponseDto createLike(Long postId);

    List<LikeResponseDto> getLikeList(Long postId);

    void deleteLike(Long likeId);
}
