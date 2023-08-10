package wanted.backend.board.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.backend.board.entity.Post;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    @NotNull
    private Long id;
    @NotNull
    private String title;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .build();
    }

}
