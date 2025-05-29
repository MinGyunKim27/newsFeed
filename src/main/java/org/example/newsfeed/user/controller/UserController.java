package org.example.newsfeed.user.controller;


import jakarta.servlet.http.HttpSession;
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

    // api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    // api/users/profile
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@RequestBody ProfileUpdateRequestDto requestDto, HttpSession session) {
        return ResponseEntity.ok(userService.updateProfile(session, requestDto));
    }

    // api/users/password
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> updatePassword(@RequestBody PasswordUpdateRequestDto requestDto, HttpSession session) {
        return ResponseEntity.ok(userService.updatePassword(session, requestDto));
    }
}
