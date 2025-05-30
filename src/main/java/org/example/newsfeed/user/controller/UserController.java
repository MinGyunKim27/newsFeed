package org.example.newsfeed.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    private final JwtProvider jwtProvider;

    // api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    // api/users/profile
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestBody ProfileUpdateRequestDto requestDto,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        return ResponseEntity.ok(userService.updateProfile(userId, requestDto));
    }

    // api/users/password
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> updatePassword(
            @RequestBody PasswordUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long Id = requestToId(request,jwtProvider);

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);
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

        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }
}
