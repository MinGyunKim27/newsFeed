package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    public LikeResponseDto createLike(Long postId) {
        //todo : 추후 UserId JWT를 이용하여 맵핑
        Long userId = 1L;
        Like like = new Like(userId, postId);
        likeRepository.save(like);

        return new LikeResponseDto(like);
    }

    @Override
    public List<LikeResponseDto> getLikeList(Long postId) {
        return likeRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(LikeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLike(Long likeId) {
        Like like = likeRepository.findById(likeId).orElseThrow();

        likeRepository.delete(like);
    }
}
