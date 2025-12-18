package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.model.Email;
import com.example.demo.domain.user.model.User;

import java.util.Optional;

//UserRepository 인터페이스는
public interface UserRepository {

    // 나는 유저 객체를 주면 저장하고, 저장된 유저를 다시 돌려줄게 기능 명세
    // void save(User user)가 아니라 User save(User user)인 이유가 중요
    // DB에 저장되는 순간 식별자(ID)가 생성됩니다. 따라서 반환되는 User 객체는 ID가 채워진 새로운 상태의 객체가 됩니다.
    User save(User user);

    // 이메일로 사용자를 찾고 반환할 거야
    Optional<User> findByEmail(Email email);

    // email이 존재하는지 참 거짓인지 boolean타입으로 반환할 거야
    boolean existsByEmail(Email email);
}
