package com.example.demo.domain.user.model;

//Password 클래스는 문자열 value를 받아서
public record Password(String value) {
    // 최소 8자 이상의 비밀번호를 입력해야 함
    private static final int MIN_LENGTH = 8;

    // 생성자에서 value가 null이거나 blank이면 IllegalArgumentException을 발생시킨다.
    public Password {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }

    // validateRawPassword 메서드는 비밀번호의 유효성을 검사하고,
    // 문자열 rawPassword가 null이거나 blank이면 IllegalArgumentException을 발생시킨다.
    // rawPassword의 길이가 MIN_LENGTH보다 작으면 IllegalArgumentException을 발생시킨다.
    public static void validateRawPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (rawPassword.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최소 " + MIN_LENGTH + "자 이상이어야 합니다.");
        }
    }

    // toString 메서드를 오버라이드하여 "[PROTECTED]"를 반환한다.
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
