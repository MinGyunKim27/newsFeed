package org.example.newsfeed.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.user.dto.PasswordUpdateRequestDto;
import org.example.newsfeed.user.dto.UpdateUserRequestDto;
import org.example.newsfeed.user.dto.UserResponseDto;
import org.example.newsfeed.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(
            @PathVariable Long id
    ){
        UserResponseDto responseDto = userService.findById(id);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Validated @RequestBody UpdateUserRequestDto requestDto,
            HttpServletRequest request
            ){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);

        UserResponseDto responseDto = userService.update(userId,requestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Validated @RequestBody PasswordUpdateRequestDto requestDto,
            HttpServletRequest request
    ){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
