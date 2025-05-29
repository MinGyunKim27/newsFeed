package org.example.newsfeed.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.dto.TokenResponse;
import org.example.newsfeed.auth.dto.WithdrawRequestDto;
import org.example.newsfeed.auth.service.AuthService;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @Validated @RequestBody SignupRequestDto requestDto
            ){
        authService.signup(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody LoginRequestDto requestDto
    ){
        User user = authService.login(requestDto);
        String token = jwtProvider.generateToken(user.getId());
        return ResponseEntity.ok(new TokenResponse(token,user.getId()));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @Validated @RequestBody WithdrawRequestDto withdrawRequestDto,
            HttpServletRequest request
    ){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtProvider.getUserId(token);
        authService.withdraw(withdrawRequestDto, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
