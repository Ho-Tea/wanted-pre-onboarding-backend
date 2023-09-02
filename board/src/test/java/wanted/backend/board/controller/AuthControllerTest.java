package wanted.backend.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.backend.board.dto.UserRequest;
import wanted.backend.board.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
//    @MockBean
//    UserService userService;


    private static final String validEmail = "Valid@email.com";
    private static final String validPassword = "12345678";
    private static final String notValid = "False";


    @DisplayName("회원가입시 이메일형식이 아닐경우 실패")
    @Test
    void validateEmailToSignUp() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(notValid)
                .password(validPassword)
                .build();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @DisplayName("회원가입시 비밀번호형식이 아닐경우 실패")
    @Test
    void validatePasswordToSignUp() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(validEmail)
                .password(notValid)
                .build();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @DisplayName("회원가입 성공")
    @Test
    void signUp() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(validEmail)
                .password(validPassword)
                .build();

        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @DisplayName("로그인시 이메일형식이 아닐경우 실패")
    @Test
    void validateEmailToLogin() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(notValid)
                .password(validPassword)
                .build();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @DisplayName("로그인시 비밀번호형식이 아닐경우 실패")
    @Test
    void validatePasswordToLogin() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(validEmail)
                .password(notValid)
                .build();
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(validEmail)
                .password(validPassword)
                .build();

        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/auth/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}