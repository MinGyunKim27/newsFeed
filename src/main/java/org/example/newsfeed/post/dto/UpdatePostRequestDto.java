package org.example.newsfeed.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.newsfeed.image.entity.Image;

import java.util.List;

@Getter
public class UpdatePostRequestDto {

    // 제목 (빈 값이 될 수 없다.) (길이는 200자 이하)
    @NotBlank(message = "제목을 반드시 입력해주세요.")
    @Size(min = 3, max = 200, message = "제목은 3자 이상, 200자 이하로 입력해주세요.")
    private String title;

    // 내용 (빈 값이 될 수 없다.) (길이는 1자 이상)
    @NotBlank(message = "내용을 반드시 입력해주세요.")
    @Size(min = 1, message = "내용은 1자 이상으로 입력해주세요.")
    private String content;

    // 이미지Url (빈 값이 될 수 있다.) (이미지는 로컬 경로 사용)
    private List<Long> imageIds; // 파일 존재 여부를 서버에서 처리
}
