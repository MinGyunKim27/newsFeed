package org.example.newsfeed.follow.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.config.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final AuthUtil authUtil;

    /**
     * 사용자 팔로우
     * GlobalExceptionHandler가 에러 자동 처리
     *
     * @param userId 팔로우할 사용자 ID
     * @return  성공 메시지
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long userId) {
        Long currentUserId = authUtil.getCurrentUserId();
        followService.followUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "팔로우 완료");
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long userId) {
        Long currentUserId = authUtil.getCurrentUserId();
        followService.unFollowUser(currentUserId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "언팔로우 완료");
        return ResponseEntity.ok(response);
    }

    
}
