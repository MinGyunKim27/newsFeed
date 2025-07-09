package org.example.newsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 댓글 수정 요청을 처리하기 위한 DTO 클래스입니다.
 * 클라이언트가 댓글 수정을 요청할 때 전달하는 데이터 형식을 정의합니다.
 */
@Getter
public class UpdateCommentRequestDto {

    /**
     * 수정할 댓글의 내용입니다.
     * 빈 문자열이거나 null일 수 없으며, 유효성 검사를 통해 필수 입력값임을 검증합니다.
     */
    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;
}
