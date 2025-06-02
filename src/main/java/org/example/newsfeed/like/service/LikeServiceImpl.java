package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.exception.*;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void createLike(Long postId, Long userId) {

        // 유저데이터 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 게시글 조회
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Like like = new Like(user, post);
        likeRepository.save(like);
    }

    @Override
    public List<LikeResponseDto> getLikeUserList(Long postId) {
        return likeRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(LikeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteLike(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Like like = likeRepository.findByUserIdAndPostId(userId, postId).orElseThrow(LikeNotFoundException::new);

        if (!user.getId().equals(like.getUser().getId())) {
            throw new NoPermissionException();
        }

        likeRepository.delete(like);
    }

    @Override
    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
