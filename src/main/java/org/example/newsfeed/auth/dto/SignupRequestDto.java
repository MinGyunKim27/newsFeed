package org.example.newsfeed.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Size(min = 8, message = "최소 8자 이상 입력해주세요!")
    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
    private String username;
}
