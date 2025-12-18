package com.example.demo.domain.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
// User 클래스는 모든 필드를 파라미터로 받는 생성자를 만든다.
public class User {

    private final Long id;
    private final Email email;
    private final Password password;
    private final String country;
    private final String phoneNumber;

    // 새 사용자 생성용 정적 팩토리 메서드
    // ID는 null (DB 저장 시 생성됨)
    public static User create(Email email, Password password, String country, String phoneNumber) {
        return new User(null, email, password, country, phoneNumber);
    }

    /**
     * DB에서 조회한 사용자 복원용 정적 팩토리 메서드
     */
    public static User reconstitute(Long id, Email email, Password password, String country, String phoneNumber) {
        return new User(id, email, password, country, phoneNumber);
    }

    /**
     * 비밀번호 매칭 확인
     * 실제 비교는 PasswordEncoder를 통해 서비스 레이어에서 처리
     */
    public boolean matchPassword(String rawPassword, PasswordMatchChecker checker) {
        return checker.matches(rawPassword, this.password.value());
    }

    /**
     * 비밀번호 매칭을 위한 함수형 인터페이스
     * 서비스 레이어에서 PasswordEncoder를 주입받아 사용
     */
    @FunctionalInterface
    public interface PasswordMatchChecker {
        boolean matches(String rawPassword, String encodedPassword);
    }
}