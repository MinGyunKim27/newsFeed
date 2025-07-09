package org.example.newsfeed.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 토큰을 생성, 검증, 파싱하는 유틸리티 클래스입니다.
 * 로그인 인증 및 사용자 식별에 사용됩니다.
 */
@Component
public class JwtProvider {

    /**
     * JWT 서명을 위한 비밀 키입니다.
     * application.properties 또는 application.yml에서 설정된 값을 주입받습니다.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 유효 기간 (밀리초 단위).
     * 예: 1000L * 60 * 60 * 24 = 24시간
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 사용자 ID를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param userId 토큰에 저장할 사용자 ID
     * @return 생성된 JWT 문자열
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId.toString())               // 사용자 ID를 subject에 저장
                .setIssuedAt(now)                            // 발급 시간
                .setExpiration(expireDate)                   // 만료 시간
                .signWith(                                   // 서명 알고리즘 및 키
                        Keys.hmacShaKeyFor(secret.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();                                  // JWT 문자열 반환
    }

    /**
     * 전달받은 JWT 토큰이 유효한지 검증합니다.
     *
     * @param token 클라이언트로부터 전달받은 JWT
     * @return 유효하면 true, 예외 발생 시 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token); // 파싱이 되면 유효한 토큰
            return true;
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰
        }
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출합니다.
     *
     * @param token 클라이언트로부터 전달받은 JWT
     * @return 토큰에 저장된 사용자 ID
     */
    public Long getUserId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(secret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject() // subject에 저장된 userId를 꺼냄
        );
    }
}
