package org.example.newsfeed.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 모든 커스텀 예외의 기본이 되는 예외 클래스입니다.
 * 예외 발생 시 HTTP 상태 코드와 메시지를 함께 포함할 수 있도록 설계되었습니다.
 * 전역 예외 처리기(@ControllerAdvice)와 함께 사용되어 클라이언트에 일관된 에러 응답을 제공합니다.
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * HTTP 상태 코드 (예: 400, 404, 500 등)
     */
    private final HttpStatus status;

    /**
     * 예외 메시지
     */
    private final String message;

    /**
     * HTTP 상태 코드와 메시지를 기반으로 예외를 생성합니다.
     *
     * @param status  HTTP 상태 코드
     * @param message 예외 메시지
     */
    public BaseException(HttpStatus status, String message) {
        super(message); // 부모 클래스 RuntimeException에 메시지를 전달
        this.status = status;
        this.message = message;
    }
}
