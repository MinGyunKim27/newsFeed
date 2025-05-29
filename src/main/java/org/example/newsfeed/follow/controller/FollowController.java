package org.example.newsfeed.follow.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.dto.FollowResponseDto;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.config.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 사용자 팔로우
     *
     * @param userId 팔로우할 사용자 ID
     * @return 성공 메시지
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long userId) {
        followService.followUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "팔로우 완료");
        return ResponseEntity.status(201).body(response);
    }

    /**
     * 사용자 언팔로우
     *
     * @param userId 언팔로우할 사용자 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long userId) {
        followService.unfollowUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "언팔로우 완료");
        return ResponseEntity.ok(response);
    }

    /**
     * 팔로잉 목록 조회 (내가 팔로우하는 사람들)
     * API 명세서에 따라 현재 로그인한 사용자의 팔로잉 목록 조회
     *
     * @return 팔로잉 목록
     */
    @GetMapping("/following")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowings() {
        List<FollowResponseDto> followings = followService.getMyFollowingList();
        return ResponseEntity.ok(followings);
    }

    /**
     * 팔로워 목록 조회 (나를 팔로우하는 사람들)
     * API 명세서에 따라 현재 로그인한 사용자의 팔로워 목록 조회
     *
     * @return 팔로워 목록
     */
    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponseDto>> getMyFollowers() {
        List<FollowResponseDto> followers = followService.getMyFollowersList();
        return ResponseEntity.ok(followers);
    }
}
