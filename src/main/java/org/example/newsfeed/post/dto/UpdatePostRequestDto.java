package org.example.newsfeed.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.newsfeed.image.entity.Image;

import java.util.List;

/**
 * 게시글 수정 요청을 받을 때 사용되는 DTO 클래스입니다.
 * 제목, 내용, 이미지 정보를 클라이언트로부터 전달받습니다.
 */
@Getter
public class UpdatePostRequestDto {

    /**
     * 게시글 제목
     * - 공백 불가 (@NotBlank)
     * - 최소 2자, 최대 200자 허용
     */
    @NotBlank(message = "제목을 반드시 입력해주세요.")
    @Size(min = 2, max = 200, message = "제목은 3자 이상, 200자 이하로 입력해주세요.")
    private String title;

    /**
     * 게시글 본문 내용
     * - 공백 불가 (@NotBlank)
     * - 최소 1자 이상
     */
    @NotBlank(message = "내용을 반드시 입력해주세요.")
    @Size(min = 1, message = "내용은 1자 이상으로 입력해주세요.")
    private String content;

    /**
     * 게시글에 첨부된 이미지의 ID 리스트
     * - 필수 아님 (null 또는 빈 리스트 허용)
     * - 프론트에서는 이미지 ID만 전달하고,
     *   실제 파일 존재 여부 및 유효성은 서버에서 처리
     */
    private List<Long> imageIds;
}
