package org.example.newsfeed.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 예외 발생 시 클라이언트에게 반환할 에러 응답 DTO입니다.
 * 예외 처리 핸들러에서 일관된 형식의 에러 정보를 반환하기 위해 사용됩니다.
 */
@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    /**
     * 에러 발생 시각 (자동 설정)
     */
    private final LocalDateTime timestamp = LocalDateTime.now();

    /**
     * HTTP 상태 코드 (예: 400, 404, 500 등)
     */
    private final int status;

    /**
     * HTTP 에러명 (예: BAD_REQUEST, NOT_FOUND 등)
     */
    private final String error;

    /**
     * 예외 상세 메시지
     */
    private final String message;

    /**
     * HttpStatus를 기반으로 에러 응답 객체를 생성하는 생성자
     *
     * @param status  HttpStatus 객체 (BAD_REQUEST, NOT_FOUND 등)
     * @param message 예외 메시지
     */
    public ErrorResponseDto(HttpStatus status, String message) {
        this.status = status.value();     // 상태 코드 (예: 400)
        this.error = status.name();       // 상태명 (예: BAD_REQUEST)
        this.message = message;           // 예외 상세 설명
    }
}
