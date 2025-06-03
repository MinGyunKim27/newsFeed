package org.example.newsfeed.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 커스텀 서블릿 필터
 * - 요청마다 한 번만 실행되는 OncePerRequestFilter를 상속
 * - multipart 요청은 필터링하지 않고 그대로 통과시킴
 */
@Component
public class CustomFilter extends OncePerRequestFilter {

    /**
     * 실제 필터링 로직 수행 메서드
     *
     * @param request     클라이언트의 HTTP 요청
     * @param response    서버의 HTTP 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // multipart/form-data 요청은 파일 업로드 처리 등으로 민감하므로 필터 건드리지 않고 통과시킴
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
            filterChain.doFilter(request, response);  // 다음 필터로 넘김
            return;
        }

        // 기타 요청도 그대로 다음 필터로 넘김 (현재는 아무 처리도 하지 않음)
        filterChain.doFilter(request, response);
    }
}
