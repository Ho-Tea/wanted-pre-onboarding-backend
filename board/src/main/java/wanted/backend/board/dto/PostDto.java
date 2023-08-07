package wanted.backend.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.backend.board.entity.Post;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String content;

    public static PostDto from(Post post){
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .build();
    }

}
