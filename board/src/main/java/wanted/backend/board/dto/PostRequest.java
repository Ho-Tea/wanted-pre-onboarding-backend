package wanted.backend.board.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.backend.board.entity.Post;
import wanted.backend.board.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    @NotNull
    private String title;
    @NotNull
    private String content;

    public Post toEntity(User user){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
