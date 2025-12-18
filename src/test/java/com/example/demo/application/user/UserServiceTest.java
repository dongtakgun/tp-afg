package com.example.demo.application.user;

import com.example.demo.application.user.dto.LoginCommand;
import com.example.demo.application.user.dto.SignUpCommand;
import com.example.demo.application.user.dto.UserResponse;
import com.example.demo.domain.user.model.Email;
import com.example.demo.domain.user.model.Password;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignUpCommand signUpCommand;
    private LoginCommand loginCommand;
    private User savedUser;

    @BeforeEach
    void setUp() {
        signUpCommand = new SignUpCommand(
                "test@example.com",
                "password123",
                "Korea",
                "010-1234-5678"
        );

        loginCommand = new LoginCommand(
                "test@example.com",
                "password123"
        );

        savedUser = User.reconstitute(
                1L,
                new Email("test@example.com"),
                new Password("encodedPassword"),
                "Korea",
                "010-1234-5678"
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        // given
        given(userRepository.existsByEmail(any(Email.class))).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        UserResponse response = userService.signUp(signUpCommand);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.country()).isEqualTo("Korea");
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signUp_Fail_DuplicateEmail() {
        // given
        given(userRepository.existsByEmail(any(Email.class))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.signUp(signUpCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 이메일");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // when
        UserResponse response = userService.login(loginCommand);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 없음")
    void login_Fail_EmailNotFound() {
        // given
        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.login(loginCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 또는 비밀번호가 올바르지 않습니다");
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_WrongPassword() {
        // given
        given(userRepository.findByEmail(any(Email.class))).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.login(loginCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 또는 비밀번호가 올바르지 않습니다");
    }
}