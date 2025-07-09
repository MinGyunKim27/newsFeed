package org.example.newsfeed.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


/**
 * 로그인 요청 정보를 받는 DTO 클래스입니다
 * 이메일과 비밀번호를 포함하며 유효성 검사 조건이 적용되어 있습니다.
 */
@Getter
public class LoginRequestDto {


    /**
     * 사용자 이메일
     * 이메일 형식을 지켜야 합니다(example@something.com)
     */
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;


    /**
     * 사용자 비밀번호
     * 비밀번호는 최소 8자 이상 입력해야 합니다.
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
}
