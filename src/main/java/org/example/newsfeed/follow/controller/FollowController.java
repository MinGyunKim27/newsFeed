package org.example.newsfeed.follow.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.dto.FollowResponseDto;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.util.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param request HTTP 요청 (Authorization 헤더 포함)
     * @return 성공 메시지
     */
    @PostMapping("/follows/{userId}")
    public ResponseEntity<Void> followUser(
            @PathVariable Long userId,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Long currentUserId = jwtProvider.getUserId(token);
        followService.followUser(currentUserId, userId);
        return ResponseEntity.status(201).body(null);
    }

    /**
     * 사용자 언팔로우
     *
     * @param userId 언팔로우할 사용자 ID
     * @param request HTTP 요청 (Authorization 헤더 포함)
     * @return 성공 메시지
     */
    @DeleteMapping("/follows/{userId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long userId,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Long currentUserId = jwtProvider.getUserId(token);
        followService.unfollowUser(currentUserId, userId);

        return ResponseEntity.ok(null);
    }

    /**
     * 팔로잉 목록 조회 (내가 팔로우하는 사람들)
     *
     * @param request HTTP 요청 (Authorization 헤더 포함)
     * @return 팔로잉 목록
     */
    @GetMapping("/{userId}/follows/following")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowings(
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Long currentUserId = jwtProvider.getUserId(token);

        List<FollowResponseDto> followings = followService.getMyFollowingList(currentUserId);
        return ResponseEntity.ok(followings);
    }

    /**
     * 팔로워 목록 조회 (나를 팔로우하는 사람들)
     *
     * @param request HTTP 요청 (Authorization 헤더 포함)
     * @return 팔로워 목록
     */
    @GetMapping("/{userId}/follows/followers")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowers(
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        Long currentUserId = jwtProvider.getUserId(token);

        List<FollowResponseDto> followers = followService.getMyFollowersList(currentUserId);
        return ResponseEntity.ok(followers);
    }
}
