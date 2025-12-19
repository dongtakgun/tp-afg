package com.example.demo.presentation.user.request;

import com.example.demo.application.user.dto.SignUpCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
        @Schema(description = "이메일", example = "test@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @Schema(description = "비밀번호 (8자 이상)", example = "password123")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Schema(description = "국가", example = "Korea")
        String country,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber
) {
    public SignUpCommand toCommand() {
        return new SignUpCommand(email, password, country, phoneNumber);
    }
}