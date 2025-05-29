package org.example.newsfeed.follow.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.dto.FollowResponseDto;
import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.follow.repository.FollowRepository;
import org.example.newsfeed.global.config.AuthUtil;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Transactional
    public void followUser(Long followerId) {
        Long currentUserId = authUtil.getCurrentUserId();

        // 사용자 존재 확인
        User follower = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        User following = userRepository.findById(followerId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        if (followRepository.existsByFollowerIdAndFollowingId(followerId)) {
            throw new BaseException(HttpStatus.CONFLICT, "이미 팔로우 중인 사용자입니다.");
        }

        // 팔로우 관계 생성 및 저장
        Follow follow = Follow.create(follower, following);
        followRepository.save(follow);
    }

    /**
     * 사용자 언팔로우
     * 팔로우 관계가 존재하는지 확인 후 삭제
     *
     * @param followerId 팔로우 하는 사용자 ID
     * @param followingId 팔로우 당하는 사용자 ID
     */
    @Transactional
    public void unFollowUser(Long followerId, Long followingId) {
        // 팔로우 관계 존재 확인
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        // 팔로우 관계 삭제
        followRepository.delete(follow);
    }

    /**
     * 팔로잉 목록 조회 (내가 팔로우 하는사람들)
     *
     * @param userId 조회할 사용자 ID
     * @return  팔로잉 목록
     */
    public List<FollowResponseDto> getFollowingList(Long userId) {
        List<Follow> followerList = followRepository.findFollowersByFollowingId(userId);

        return followerList.stream()
                .map(follow ->  new FollowResponseDto(follow.getFollower()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로워 목록 조회 (나를 팔로우하는 사람들)
     *
     * @param userId 조회할 사용자 ID
     * @return 팔로워 목록
     */
    public List<FollowResponseDto> getFollowersList(Long userId) {
        List<Follow> followersList = followRepository.findFollowersByFollowingId(userId);

        return followersList.stream()
                .map(follow -> new FollowResponseDto(follow.getFollower()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로우 관계 확인
     *
     * @param followerId  팔로우하는 사용자 ID
     * @param followingId 팔로우 당하는 사용자 ID
     * @return 팔로우 관계 존재 여부
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * 팔로잉 수 조회
     *
     * @param userId 사용자 ID
     * @return 팔로잉 수
     */
    public long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    /**
     * 팔로워 수 조회
     *
     * @param userId 사용자 ID
     * @return 팔로워 수
     */
    public long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}
