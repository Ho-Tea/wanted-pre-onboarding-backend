package wanted.backend.board.service;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import wanted.backend.board.dto.UserRequest;
import wanted.backend.board.entity.User;
import wanted.backend.board.repository.UserRepository;
import wanted.backend.board.vo.Authority;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static UserRequest user;
    @BeforeEach
    void setup(){
        user = UserRequest.builder()
                .email("Test@test")
                .password("12345678")
                .build();
        userService.join(user);
    }

    @DisplayName("비밀번호는 암호화하여 저장되어야 한다")
    @Test
    void validateEncoderPassword(){
        String encryptPassword = userRepository.findByEmail(user.getEmail()).get().getPassword();
        assertThat(passwordEncoder.matches(user.getPassword(), encryptPassword)).isTrue();
    }

    @DisplayName("가입되어있지 않은 사용자가 로그인 시 예외 발생")
    @Test
    void validateRegisteredUser(){
        UserRequest newUser = UserRequest.builder()
                .email("new@com")
                .password("12345678").build();
        assertThatThrownBy(() -> userService.login(newUser)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("회원가입시 중복된 회원이 있을경우 예외 발생")
    @Test
    void validateDuplicateUser(){
        UserRequest newUser = UserRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword()).build();
        assertThatThrownBy(() -> userService.join(newUser)).isInstanceOf(RuntimeException.class);
    }


}