package com.example.demo.application.user;

import com.example.demo.application.user.dto.LoginCommand;
import com.example.demo.application.user.dto.SignUpCommand;
import com.example.demo.application.user.dto.UserResponse;
import com.example.demo.domain.user.model.Email;
import com.example.demo.domain.user.model.Password;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse signUp(SignUpCommand command) {
        Email email = new Email(command.email());


        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + command.email());
        }

        Password.validateRawPassword(command.password());

        String encodedPassword = passwordEncoder.encode(command.password());
        Password password = new Password(encodedPassword);

        User user = User.create(email, password, command.country(), command.phoneNumber());

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }


    public UserResponse login(LoginCommand command) {
        Email email = new Email(command.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        boolean isPasswordMatch = user.matchPassword(
                command.password(),
                passwordEncoder::matches
        );

        if (!isPasswordMatch) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return UserResponse.from(user);
    }
}


