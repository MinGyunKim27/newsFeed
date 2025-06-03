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

/**
 * 좋아요 관련 비즈니스 로직을 구현한 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 좋아요 생성
     * 해당 게시글에 대해 현재 사용자가 좋아요를 누릅니다.
     *
     * @param postId 대상 게시글 ID
     * @param userId 요청한 사용자 ID
     * @throws UserNotFoundException 유저가 존재하지 않는 경우
     * @throws PostNotFoundException 게시글이 존재하지 않는 경우
     */
    @Transactional
    @Override
    public void createLike(Long postId, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 게시글 조회
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        // 좋아요 생성 및 저장
        Like like = new Like(user, post);
        likeRepository.save(like);
    }

    /**
     * 특정 게시글을 좋아요한 사용자 목록 조회
     *
     * @param postId 게시글 ID
     * @return 사용자 정보를 담은 DTO 리스트 (최신순 정렬)
     */
    @Override
    public List<LikeResponseDto> getLikeUserList(Long postId) {
        return likeRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(LikeResponseDto::new) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    /**
     * 좋아요 취소
     * 사용자가 해당 게시글에 누른 좋아요를 취소합니다.
     *
     * @param postId 게시글 ID
     * @param userId 요청한 사용자 ID
     * @throws UserNotFoundException 유저가 존재하지 않는 경우
     * @throws LikeNotFoundException 좋아요가 존재하지 않는 경우
     * @throws NoPermissionException 본인이 누른 좋아요가 아닌 경우
     */
    @Transactional
    @Override
    public void deleteLike(Long postId, Long userId) {
        // 사용자 유효성 검사
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 해당 사용자 + 게시글에 대한 좋아요 엔티티 조회
        Like like = likeRepository.findByUserIdAndPostId(userId, postId).orElseThrow(LikeNotFoundException::new);

        // 권한 확인: 본인의 좋아요만 삭제 가능
        if (!user.getId().equals(like.getUser().getId())) {
            throw new NoPermissionException();
        }

        // 좋아요 삭제
        likeRepository.delete(like);
    }

    /**
     * 게시글의 총 좋아요 수 조회
     *
     * @param postId 게시글 ID
     * @return 좋아요 수
     */
    @Override
    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
