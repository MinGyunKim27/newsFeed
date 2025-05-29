package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.global.exception.LikeNotFoundException;
import org.example.newsfeed.global.exception.PostNotFoundException;
import org.example.newsfeed.global.exception.UserNotFoundException;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.post.entitiy.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public LikeResponseDto createLike(Long postId, Long userId) {

        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 게시글 조회
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Like like = new Like(user, post);
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
    public void deleteLike(Long likeId, Long userId) {
        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Like like = likeRepository.findById(likeId).orElseThrow(LikeNotFoundException::new);

        if (!user.getId().equals(like.getUser().getId())) {
            throw new BaseException(HttpStatus.FORBIDDEN, "권한이 없습니다");
        }

        likeRepository.delete(like);
    }
}
