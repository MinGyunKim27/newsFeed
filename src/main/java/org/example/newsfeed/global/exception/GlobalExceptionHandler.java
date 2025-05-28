package org.example.newsfeed.global.exception;

import org.example.newsfeed.global.common.dto.ErrorResponseDto;
import org.example.newsfeed.image.exception.ImageUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    // ValidException 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // BaseException 상속받는 예외처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handlePlannerException(BaseException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> handleImageUploadException(ImageUploadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 업로드 실패: " + ex.getMessage());
    }
}
