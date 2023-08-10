package wanted.backend.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import wanted.backend.board.dto.PostRequest;
import wanted.backend.board.dto.PostResponse;
import wanted.backend.board.dto.UserAdapter;
import wanted.backend.board.entity.Post;
import wanted.backend.board.entity.User;
import wanted.backend.board.repository.PostRepository;
import wanted.backend.board.repository.UserRepository;
import wanted.backend.board.vo.Authority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Mock
    static UserAdapter userAdapter;

    private static final String EMAIL = "Test@com";

    @BeforeEach
    void setup() {
        User user = User.builder()
                .email(EMAIL)
                .password("12345678")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(user);
        when(userAdapter.getUser()).thenReturn(user);
    }

    private void createPost(int count) {
        for (int i = 0; i < count; i++) {
            Post post = Post.builder()
                    .title("Title " + i)
                    .content("Content " + i)
                    .user(userAdapter.getUser())
                    .build();
            postRepository.save(post);
        }
    }


    @DisplayName("게시물 목록을 조회할 때 Pagination기능을 사용한다")
    @Test
    @Rollback
    void validatePagination() {
        createPost(10);
        int page = 1;
        int size = 3;
        Page<PostResponse> posts = postService.searchAll(page, size);
        int postCount = posts.getContent().size();
        int pageIndex = posts.getNumber() + 1;
        assertThat(postCount).isEqualTo(size);
        assertThat(pageIndex).isEqualTo(page);
    }

    @DisplayName("게시글 생성 검증")
    @Test
    @Rollback
    void validateCreate() {
        createPost(10);
        int expect = postRepository.findAll().size() + 1;

        PostRequest newPost = PostRequest.builder()
                .title("newTitle")
                .content("newContent").build();
        postService.save(newPost, userAdapter);

        int result = postRepository.findAll().size();
        assertThat(result).isEqualTo(expect);
    }


    @DisplayName("게시글 수정 시 게시글 작성자가 아니면 예외발생 ")
    @Test
    @Rollback
    void validatePostUpdateByOtherUser() {
        createPost(10);
        Long postId = 1L;
        User newUser = User.builder()
                .email("newUser@com")
                .password("12345678")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(newUser);
        when(userAdapter.getUser()).thenReturn(newUser);

        assertThrows(RuntimeException.class,
                () -> postService.update(postId, new PostRequest("new", "new"), userAdapter));
    }


    @DisplayName("게시글 삭제 시 게시글 작성자가 아니면 예외발생")
    @Test
    void validatePostDeleteByOtherUser() {
        createPost(10);
        Long postId = 1L;
        User newUser = User.builder()
                .email("newUser@com")
                .password("12345678")
                .authority(Authority.ROLE_USER)
                .build();
        userRepository.save(newUser);
        when(userAdapter.getUser()).thenReturn(newUser);

        assertThrows(RuntimeException.class,
                () -> postService.delete(postId, userAdapter));
    }


}