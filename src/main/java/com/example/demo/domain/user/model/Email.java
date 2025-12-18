package com.example.demo.domain.user.model;

import java.util.regex.Pattern;

//Email 클래스는 문자열 value를 받아서
public record Email(String value) {
    // 정규식 패턴을 컴파일하여 이메일 형식을 검사
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Email 생성자 value가 null이거나 blank이면 IllegalArgumentException을 발생시킨다.
    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        // value가 이메일 형식이 아니면 IllegalArgumentException을 발생시킨다.
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다: " + value);
        }
    }

    // toString 메서드를 오버라이드하여 value를 반환한다.
    @Override
    public String toString() {
        return value;
    }
}
