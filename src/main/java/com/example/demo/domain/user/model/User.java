package com.example.demo.domain.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final Long id;
    private final Email email;
    private final Password password;
    private final String country;
    private final String phoneNumber;


    public static User create(Email email, Password password, String country, String phoneNumber) {
        return new User(null, email, password, country, phoneNumber);
    }


    public static User reconstitute(Long id, Email email, Password password, String country, String phoneNumber) {
        return new User(id, email, password, country, phoneNumber);
    }


    public boolean matchPassword(String rawPassword, PasswordMatchChecker checker) {
        return checker.matches(rawPassword, this.password.value());
    }


    @FunctionalInterface
    public interface PasswordMatchChecker {
        boolean matches(String rawPassword, String encodedPassword);
    }
}