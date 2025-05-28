package org.example.newsfeed.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
    private String username;

    @NotNull(message = "이미지 주소는 필수 입력 항목입니다.")
    private String profileImage;

    @NotNull(message = "자기소개는 필수 입력 항목입니다.")
    private String bio;
}
