package wanted.backend.board.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.backend.board.dto.UserRequest;
import wanted.backend.board.entity.User;
import wanted.backend.board.jwt.TokenProvider;
import wanted.backend.board.repository.UserRepository;
import wanted.backend.board.vo.Authority;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public String login(UserRequest userDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }

    @Transactional
    public String join(UserRequest userDto) {
        validateDuplicateUser(userDto);
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        userRepository.save(user);
        return user.getEmail();
    }

    private void validateDuplicateUser(UserRequest userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입 되어있는 유저입니다");
        }
    }
}