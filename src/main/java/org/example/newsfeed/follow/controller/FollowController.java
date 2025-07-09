package org.example.newsfeed.follow.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.dto.FollowResponseDto;
import org.example.newsfeed.follow.dto.IsFollowResponseDto;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.util.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.newsfeed.global.util.RequestToId.requestToId;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final JwtProvider jwtProvider;

    /**
     * 사용자 팔로우
     *
     * @param userId 팔로우할 사용자 ID
     * @return 성공 메시지
     */
    @PostMapping("/follows/{userId}")
    public ResponseEntity<Void> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal long currentUserId) {

        followService.followUser(currentUserId, userId);
        return ResponseEntity.status(201).body(null);
    }

    /**
     * 사용자 언팔로우
     *
     * @param userId 언팔로우할 사용자 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/follows/{userId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal long currentUserId) {

        followService.unfollowUser(currentUserId, userId);

        return ResponseEntity.ok(null);
    }

    /**
     * 팔로잉 목록 조회 (내가 팔로우하는 사람들)
     *
     * @return 팔로잉 목록
     */
    @GetMapping("/{userId}/follows/following")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowings(
            @AuthenticationPrincipal long currentUserId) {

        List<FollowResponseDto> followings = followService.getMyFollowingList(currentUserId);
        return ResponseEntity.ok(followings);
    }

    /**
     * 팔로워 목록 조회 (나를 팔로우하는 사람들)
     *
     * @param currentUserId 인증된 사용자 ID (JWT 기반 @AuthenticationPrincipal에서 추출)
     * @return 팔로워 목록
     */
    @GetMapping("/{userId}/follows/followers")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowers(
            @AuthenticationPrincipal long currentUserId) {

        List<FollowResponseDto> followers = followService.getMyFollowersList(currentUserId);
        return ResponseEntity.ok(followers);
    }


    /**
     * 팔로우 관게 확인
     *
     * @param userId 롹인하려는 사용자 ID
     * @param currentId 인증된 사용자 ID (JWT 기반 @AuthenticationPrincipal에서 추출)
     * @return Boolean 값 팔로우 중이면 true, 아니면 false
     */
    @GetMapping("/follows/{userId}/isFollow")
    public ResponseEntity<IsFollowResponseDto> validIsFollow(
            @PathVariable Long userId,
            @AuthenticationPrincipal long currentId
    ){
        boolean isFollow = followService.isFollowing(currentId,userId);
        IsFollowResponseDto isFollowResponseDto = new IsFollowResponseDto(isFollow);
        return new ResponseEntity<>(isFollowResponseDto, HttpStatus.OK);
    }

}
