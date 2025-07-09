package org.example.newsfeed.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.util.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 토큰을 검사하고 인증 정보를 SecurityContext에 설정하는 필터입니다.
 * Spring Security에서 매 요청마다 한 번만 실행됩니다.
 */
@Component // Spring Bean으로 등록
@RequiredArgsConstructor // final 필드인 jwtProvider를 생성자 주입
public class JwtFilter extends OncePerRequestFilter {

    // JWT 토큰의 생성, 파싱, 검증을 담당하는 유틸 클래스
    private final JwtProvider jwtProvider;

    /**
     * 실제 필터링 로직이 수행되는 메서드입니다.
     * 요청 헤더에서 JWT를 추출하고 유효성을 검사한 후,
     * 유효하면 SecurityContext에 인증 정보를 저장합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인을 통해 다음 필터로 요청을 전달
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Authorization 헤더에서 JWT 토큰 추출
        String token = getToken(request);

        // 토큰이 존재하고 유효하면
        if (token != null && jwtProvider.validateToken(token)) {
            // 토큰에서 사용자 ID 추출
            Long userId = jwtProvider.getUserId(token);

            // 사용자 정보를 기반으로 인증 객체 생성 (권한은 비워둠)
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

            // SecurityContext에 인증 객체 저장 -> 이후 컨트롤러에서 @AuthenticationPrincipal로 사용 가능
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 다음 필터로 요청 전달 (체인 이어주기)
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰만 추출하는 메서드입니다.
     *
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 또는 null
     */
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        // Bearer [토큰] 형식일 때만 토큰 추출
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 문자열 반환
        }
        return null;
    }
}
