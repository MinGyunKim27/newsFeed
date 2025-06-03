package org.example.newsfeed.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.auth.dto.LoginRequestDto;
import org.example.newsfeed.auth.dto.SignupRequestDto;
import org.example.newsfeed.auth.dto.TokenResponse;
import org.example.newsfeed.auth.dto.WithdrawRequestDto;
import org.example.newsfeed.auth.service.AuthService;
import org.example.newsfeed.follow.service.FollowService;
import org.example.newsfeed.global.util.JwtProvider;
import org.example.newsfeed.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.example.newsfeed.global.util.RequestToId.requestToId;


/**
 * 인증 관련 요청을 처리하는 컨트롤러 입니다
 * 회원가입, 로그인, 로그아웃 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final AuthUtil authUtil;
    private final FollowService followService;


    /**
     * 사용자 회원가입을 처리합니다.
     *
     * @param requestDto 회원가입 요청 정보(이름, 이메일 비밀번호)
     * @return HttpStatus.Created
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @Validated @RequestBody SignupRequestDto requestDto
            ){
        authService.signup(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 사용자 로그인을 진행합니다.
     *
     * @param requestDto 사용자 로그인 정보(이메일, 비밀번호)
     * @return 토큰과 Http 상태를 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody LoginRequestDto requestDto
    ){
        User user = authService.login(requestDto);
        String token = jwtProvider.generateToken(user.getId());
        return ResponseEntity.ok(new TokenResponse(token,user.getId()));
    }

    /**
     * 사용자 회원 탈퇴를 진행합니다
     *
     * @param withdrawRequestDto 사용자 비밀번호 정보
     * @param request 서블릿 리퀘스트(토큰 추출용)
     * @return 상태코드 반환
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @Validated @RequestBody WithdrawRequestDto withdrawRequestDto,
            HttpServletRequest request
    ){
        Long userId = requestToId(request,jwtProvider);
        followService.deleteAllFollowByUserId(userId);
        authService.withdraw(withdrawRequestDto, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
