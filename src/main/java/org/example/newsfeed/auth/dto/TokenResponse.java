package org.example.newsfeed.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 토큰 정보를 반환하기 위한 DTO 레코드 클래스
 * 토큰 문자열과 토큰 아이디를 가지고 있습니다.
 */
public record TokenResponse(String accessToken, Long userId) {
}
