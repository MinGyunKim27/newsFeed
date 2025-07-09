package org.example.newsfeed.global.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest 객체에서 JWT 토큰을 추출하고,
 * 해당 토큰으로부터 사용자 ID를 반환하는 유틸리티 클래스입니다.
 */
public class RequestToId {

    /**
     * 요청 헤더에서 JWT 토큰을 추출하고, 해당 토큰에서 사용자 ID를 반환합니다.
     * Authorization 헤더의 값은 "Bearer {토큰}" 형식이어야 합니다.
     *
     * @param request  클라이언트의 HTTP 요청 객체
     * @param provider JWT 토큰에서 사용자 ID를 추출할 수 있는 JwtProvider 객체
     * @return 사용자 ID
     */
    public static Long requestToId(HttpServletRequest request, JwtProvider provider) {
        // "Authorization: Bearer {token}" 형식에서 "Bearer "를 제거하고 토큰만 추출
        String token = request.getHeader("Authorization").substring(7);
        return provider.getUserId(token);
    }
}
