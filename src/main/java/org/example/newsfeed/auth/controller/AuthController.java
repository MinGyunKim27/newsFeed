package org.example.newsfeed.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody SignupRequestDto requestDto
            ){
        authService.signup(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequestDto requestDto
    ){
        authService.login(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
