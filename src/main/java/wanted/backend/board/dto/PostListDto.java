package wanted.backend.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wanted.backend.board.vo.PageInfo;

@Getter
@AllArgsConstructor
public class PostListDto<T> {
    private T posts;
    private PageInfo pageInfo;
}
