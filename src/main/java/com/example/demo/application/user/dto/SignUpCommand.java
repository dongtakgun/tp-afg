package com.example.demo.application.user.dto;

//SignUpCommannd는 회원가입을 위한 명세를 담는 클래스
public record SignUpCommand(
                String email,
                String password,
                String country,
                String phoneNumber) {
}
