package com.example.demo.application.user.dto;

//LoginCommand는 로그인을 위한 명세를 담는 클래스
public record LoginCommand(
                String email,
                String password) {
}
