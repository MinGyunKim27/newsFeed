package org.example.newsfeed.user.controller;


import lombok.RequiredArgsConstructor;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.ProfileUpdateRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestParam Long userId, @RequestBody ProfileUpdateRequestDto requestDto) {
        userService.updateProfile(userId, requestDto);
        return ResponseEntity.ok("프로필 수정 완료");
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestParam Long userId, @RequestBody PasswordUpdateRequestDto requestDto) {
        userService.updatePassword(userId, requestDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }
}
