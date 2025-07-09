package org.example.newsfeed.global.config;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.global.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 담당하는 구성 클래스입니다.
 * JWT 인증 기반의 보안 설정을 구성합니다.
 */
@Configuration // 스프링 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security 설정을 활성화함
@RequiredArgsConstructor // 생성자 주입을 위한 Lombok 어노테이션 (jwtFilter 주입)
public class SecurityConfig {

    // JWT 토큰 검증을 위한 커스텀 필터
    private final JwtFilter jwtFilter;

    /**
     * HTTP 보안 설정을 구성하는 메서드입니다.
     * - CSRF 비활성화
     * - 인증 필요 없는 URL 설정 (정적 자원, 로그인/회원가입 등)
     * - 나머지 요청은 인증 필요
     * - JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (JWT 기반 인증에서는 불필요)
                .csrf(AbstractHttpConfigurer::disable)

                // 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 다음 경로들은 인증 없이 접근 허용
                        .requestMatchers(
                                "/api/auth/signup",       // 회원가입 API
                                "/api/auth/login",        // 로그인 API
                                "/signup.html",           // 회원가입 페이지
                                "/login.html",            // 로그인 페이지
                                "/mypage.html",           // 마이페이지
                                "/edit-profile.html",     // 프로필 수정 페이지
                                "/newsfeed.html",         // 뉴스피드 페이지
                                "/create-post.html",      // 게시물 작성 페이지
                                "/search.html",           // 검색 페이지
                                "/user-comments.html",    // 유저 댓글 페이지
                                "/user-profile.html",     // 유저 프로필 페이지
                                "/favicon.ico",           // 파비콘
                                "/images/**",             // 이미지 리소스
                                "/css/**",                // CSS 리소스
                                "/js/**"                  // JS 리소스
                        ).permitAll()

                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 삽입
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 구성된 보안 필터 체인을 반환
        return http.build();
    }
}
