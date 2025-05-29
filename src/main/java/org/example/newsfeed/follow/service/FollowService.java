package org.example.newsfeed.follow.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.dto.FollowResponseDto;
import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.follow.repository.FollowRepository;
import org.example.newsfeed.global.config.AuthUtil;
import org.example.newsfeed.global.exception.BaseException;
import org.example.newsfeed.user.entity.User;
import org.example.newsfeed.user.repository.UserRepository;
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

    /**
     * 사용자 팔로우
     * 현재 로그인한 사용자가 특정 사용자를 팔로우
     *
     * @param followingId 팔로우할 사용자 ID
     * @throws BaseException 사용자가 존재하지 않거나 이미 팔로우 중인 경우
     */
    @Transactional
    public void followUser(Long followingId) {
        Long currentUserId = authUtil.getCurrentUserId();

        //사용자 존재 확인
        User follower = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "현재 사용자를 찾을 수 없습니다."));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "팔로우 하려는 사용자를 찾을 수 없습니다."));

        // 중복 팔로우 확인
        if (followRepository.existsByFollowerIdAndFollowingId(currentUserId, followingId)) {
            throw new BaseException(HttpStatus.CONFLICT, "이미 팔로우 중인 사용자입니다.");
        }

        Follow follow = Follow.create(follower, following);
        followRepository.save(follow);
    }

    /**
     * 사용자 언팔로우
     * 현재 로그인한 사용자가 특정 사용자를 언팔로우
     *
     * @param followingId 언팔로우할 사용자 ID
     * @throws BaseException 팔로우 관계가 존재하지 않는 경우
     */
    @Transactional
    public void unfollowUser(Long followingId) {
        Long currentUserId = authUtil.getCurrentUserId();

        Follow follow = followRepository.findByFollowerIdAndFollowingId(currentUserId, followingId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, "팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);
    }

    /**
     * 내 팔로잉 목록 조회 (내가 팔로우하는 사람들)
     * API 명세서에 따라 현재 로그인한 사용자의 팔로잉 목록만 조회
     *
     * @return 팔로잉 목록
     */
    public List<FollowResponseDto> getMyFollowingList() {
        Long currentUserId = authUtil.getCurrentUserId();

        List<Follow> followingList = followRepository.findFollowingByFollowerId(currentUserId);

        return followingList.stream()
                .map(follow -> new FollowResponseDto(follow.getFollowing()))
                .collect(Collectors.toList());
    }

    /**
     * 내 팔로워 목록 조회 (나를 팔로우하는 사람들)
     * API 명세서에 따라 현재 로그인한 사용자의 팔로워 목록만 조회
     *
     * @return 팔로워 목록
     */
    public List<FollowResponseDto> getMyFollowersList() {
        Long currentUserId = authUtil.getCurrentUserId();

        List<Follow> followersList = followRepository.findFollowersByFollowingId(currentUserId);

        return followersList.stream()
                .map(follow -> new FollowResponseDto(follow.getFollower()))
                .collect(Collectors.toList());
    }

    /**
     * 회원 탈퇴 시 해당 사용자의 모든 팔로우 관계 삭제
     * UserService에서 회원 탈퇴 시 호출됨
     *
     * @param userId 탈퇴하는 사용자 ID
     */
    @Transactional
    public void deleteAllFollowByUserId(Long userId) {
        followRepository.deleteAllByUserId(userId);
    }

    /**
     * 팔로우 관계 확인
     *
     * @param followerId 팔로우하는 사용자 ID
     * @param followingId 팔로우 당하는 사용자 ID
     * @return 팔로우 관계 존재 여부
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * 팔로잉 수 조회 (내가 팔로우하는 사람 수)
     *
     * @param userId 사용자 ID
     * @return 팔로잉 수
     */
    public long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    /**
     * 팔로워 수 조회 (나를 팔로우하는 사람 수)
     *
     * @param userId 사용자 ID
     * @return 팔로워 수
     */
    public long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

}
