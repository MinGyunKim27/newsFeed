package org.example.newsfeed.global.exception;

import org.example.newsfeed.global.common.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 클래스입니다.
 * 컨트롤러에서 발생하는 예외들을 일괄적으로 처리하여 일관된 응답을 제공합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 어노테이션 검증 실패 시 발생하는 예외 처리
     *
     * @param ex MethodArgumentNotValidException
     * @return 각 필드별 오류 메시지를 포함한 응답 (HTTP 400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        // 모든 필드 오류 정보를 추출하여 Map에 저장
        ex.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(
                        ((FieldError) error).getField(),
                        error.getDefaultMessage()
                ));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * 커스텀 예외(BaseException) 처리
     * 서비스, 도메인 계층 등에서 발생한 비즈니스 예외를 처리합니다.
     *
     * @param e BaseException (커스텀 예외 클래스)
     * @return 상태코드와 메시지를 포함한 에러 응답
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handlePlannerException(BaseException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatus());
    }

    /**
     * IllegalArgumentException 처리
     * 주로 잘못된 파라미터나 조건 위반 등에서 발생
     *
     * @param e IllegalArgumentException
     * @return 오류 메시지를 포함한 응답 (HTTP 400 Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
