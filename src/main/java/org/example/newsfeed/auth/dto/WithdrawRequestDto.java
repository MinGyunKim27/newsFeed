package org.example.newsfeed.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WithdrawRequestDto {

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
}
