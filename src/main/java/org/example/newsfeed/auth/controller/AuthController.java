package org.example.newsfeed.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.dto.TokenResponse;
import org.example.newsfeed.auth.dto.WithdrawRequestDto;
import org.example.newsfeed.auth.service.AuthService;
import org.example.newsfeed.global.util.JwtTokenProvider;
import org.example.newsfeed.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody SignupRequestDto requestDto
            ){
        authService.signup(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto requestDto
    ){
        User user = authService.login(requestDto);
        String token = jwtTokenProvider.createToken(user.getId());
        return ResponseEntity.ok(new TokenResponse(token,user.getId()));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @RequestBody WithdrawRequestDto withdrawRequestDto,
            @AuthenticationPrincipal Long userId
    ){

        authService.withdraw(withdrawRequestDto, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
