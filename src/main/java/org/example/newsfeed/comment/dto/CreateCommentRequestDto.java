package org.example.newsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank; // javax.validation의 Jakarta EE 버전. 필드 검증을 위한 어노테이션 사용
import lombok.Getter; // Lombok에서 제공하는 Getter 자동 생성 어노테이션

/**
 * 댓글 생성 요청을 받을 때 사용하는 DTO 클래스입니다.
 * 클라이언트로부터 content 필드만 입력받습니다.
 */
@Getter // content 필드의 getter 메서드를 Lombok이 자동 생성합니다.
public class CreateCommentRequestDto {

    /**
     * 댓글 내용 필드입니다.
     * 빈 문자열이나 공백만 있는 경우에는 유효성 검사에서 실패합니다.
     * 메시지는 "내용은 필수 입력값입니다"로 설정되어 있습니다.
     */
    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;
}
