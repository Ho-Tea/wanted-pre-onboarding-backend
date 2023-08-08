package wanted.backend.board.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfo {
    private int size;
    private int page;
    private int totalElements;
    private int totalPages;
}
