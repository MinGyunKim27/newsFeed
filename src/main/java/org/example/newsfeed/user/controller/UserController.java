package org.example.newsfeed.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.util.JwtTokenProvider;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.UpdateUserRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(
            @PathVariable Long id
    ){
        UserResponseDto responseDto = userService.findById(id);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestBody UpdateUserRequestDto requestDto,
            @AuthenticationPrincipal Long userId
            ){

        UserResponseDto responseDto = userService.update(userId,requestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody PasswordUpdateRequestDto requestDto,
            @AuthenticationPrincipal Long userId
    ){
        userService.updatePassword(userId,requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
