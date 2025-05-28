package org.example.newsfeed.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String currentPassword;

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String newPassword;
}
