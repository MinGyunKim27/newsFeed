package org.example.newsfeed.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.newsfeed.image.entity.Image;

import java.util.List;

/**
 * 게시글 생성 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO 클래스입니다.
 * 유효성 검증 어노테이션을 통해 제목과 내용의 입력 조건을 지정합니다.
 */
@Getter // 모든 필드에 대한 getter 메서드를 Lombok이 자동 생성
public class CreatePostRequestDto {

    /**
     * 게시글 제목 필드입니다.
     * - 공백 불가
     * - 최소 3자 이상, 최대 200자 이하
     */
    @NotBlank(message = "제목을 반드시 입력해주세요.")
    @Size(min = 3, max = 200, message = "제목은 3자 이상, 200자 이하로 입력해주세요.")
    private String title;

    /**
     * 게시글 내용 필드입니다.
     * - 공백 불가
     * - 최소 1자 이상
     */
    @NotBlank(message = "내용을 반드시 입력해주세요.")
    @Size(min = 1, message = "내용은 1자 이상으로 입력해주세요.")
    private String content;

    /**
     * 첨부 이미지의 ID 리스트입니다.
     * - 이 필드는 비어 있어도 무방함 (nullable)
     * - 실제 이미지 데이터는 ID를 통해 서버에서 조회
     */
    private List<Long> imageIds; // 클라이언트에서 업로드한 이미지의 ID 목록
}
