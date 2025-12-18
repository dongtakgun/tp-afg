package com.example.demo.presentation.user.request;

import com.example.demo.application.user.dto.SignUpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        String country,

        String phoneNumber
) {
    public SignUpCommand toCommand() {
        return new SignUpCommand(email, password, country, phoneNumber);
    }
}