package org.example.newsfeed.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.global.util.RequestToId;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.ProfileUpdateRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.newsfeed.global.util.RequestToId.requestToId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final JwtProvider jwtProvider;

    // api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {

        Long followerCount = followService.getFollowerCount(userId);
        UserResponseDto userResponseDto = userService.getUser(userId);
        userResponseDto.setFollowerCount(followerCount);

        return ResponseEntity.ok(userResponseDto);
    }

    // api/users/profile
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestBody ProfileUpdateRequestDto requestDto,
            HttpServletRequest request) {

        Long userId = requestToId(request,jwtProvider);
        Long followerCount = followService.getFollowerCount(userId);

        UserResponseDto userResponseDto = userService.updateProfile(userId, requestDto);
        userResponseDto.setFollowerCount(followerCount);

        return ResponseEntity.ok(userResponseDto);
    }

    // api/users/password
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> updatePassword(
            @RequestBody PasswordUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = requestToId(request,jwtProvider);
        return ResponseEntity.ok(userService.updatePassword(userId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findUsers(
            @RequestParam (required = false) String username,
            HttpServletRequest request
    ){
        Long userId = requestToId(request,jwtProvider);
        String keyword = (username == null) ? "" : username;
        List<UserResponseDto> userResponseDtoList = userService.findUsers(keyword,userId);


        // 각 유저에 대해 followerCount 설정
        for (UserResponseDto userDto : userResponseDtoList) {
            Long targetUserId = userDto.getUserId();
            Long followerCount = followService.getFollowerCount(targetUserId);
            userDto.setFollowerCount(followerCount);
        }

        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }
}
