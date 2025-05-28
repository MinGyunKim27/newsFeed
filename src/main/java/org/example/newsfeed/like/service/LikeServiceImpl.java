package org.example.newsfeed.like.service;

import org.example.newsfeed.like.dto.LikeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Override
    public LikeResponseDto createLike(Long postId) {
        return null;
    }

    @Override
    public List<LikeResponseDto> getLikeList(Long postId) {
        return List.of();
    }

    @Override
    public void deleteLike(Long likeId) {

    }
}
