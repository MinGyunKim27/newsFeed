package org.example.newsfeed.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.ProfileUpdateRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.example.newsfeed.global.util.RequestToId.requestToId;

/**
 * 사용자 관련 API 요청을 처리하는 컨트롤러입니다.
 * - 사용자 정보 조회
 * - 프로필 수정
 * - 비밀번호 변경
 * - 사용자 검색
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final JwtProvider jwtProvider;

    /**
     * 사용자 정보 조회 API
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보 + 팔로워 수를 포함한 응답 DTO
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        Long followerCount = followService.getFollowerCount(userId);         // 팔로워 수 조회
        UserResponseDto userResponseDto = userService.getUser(userId);       // 사용자 정보 조회
        userResponseDto.setFollowerCount(followerCount);                     // 팔로워 수 추가

        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자 프로필 수정 API
     *
     * @param requestDto 프로필 수정 요청 DTO (닉네임, 소개글 등)
     * @param request HTTP 요청 객체 (Authorization 헤더 포함)
     * @return 수정된 사용자 정보 + 팔로워 수 포함 응답 DTO
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Validated @RequestBody ProfileUpdateRequestDto requestDto,
            HttpServletRequest request) {

        Long userId = requestToId(request, jwtProvider);                     // JWT에서 userId 추출
        Long followerCount = followService.getFollowerCount(userId);        // 팔로워 수 조회

        UserResponseDto userResponseDto = userService.updateProfile(userId, requestDto); // 프로필 업데이트
        userResponseDto.setFollowerCount(followerCount);                     // 팔로워 수 추가

        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자 비밀번호 변경 API
     *
     * @param requestDto 현재 비밀번호 + 새 비밀번호를 담은 DTO
     * @param request HTTP 요청 객체 (Authorization 헤더 포함)
     * @return 비밀번호 변경 후 사용자 정보 반환
     */
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> updatePassword(
            @Validated @RequestBody PasswordUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = requestToId(request, jwtProvider);                     // JWT에서 userId 추출
        return ResponseEntity.ok(userService.updatePassword(userId, requestDto));
    }

    /**
     * 사용자 검색 API (팔로워 수 포함)
     *
     * @param username 검색할 사용자 이름 (선택값)
     * @param page 페이지 번호 (기본값 0)
     * @param size 페이지 크기 (기본값 0)
     * @param request HTTP 요청 객체 (Authorization 헤더 포함)
     * @return 검색된 사용자 리스트 (페이지 형태)
     */
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            HttpServletRequest request
    ) {
        Long userId = requestToId(request, jwtProvider);                     // JWT에서 userId 추출
        String keyword = (username == null) ? "" : username;                // 검색어 없으면 빈 문자열 처리

        Page<UserResponseDto> result = userService.findUsersWithFollowerCount(keyword, userId, page, size);
        return ResponseEntity.ok(result);
    }
}
