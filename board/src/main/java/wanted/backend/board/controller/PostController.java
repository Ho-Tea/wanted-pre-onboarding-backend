package wanted.backend.board.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.backend.board.dto.PostResponse;
import wanted.backend.board.repository.PostRepository;
import wanted.backend.board.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/searchAll")
    public ResponseEntity<Result<Page<PostResponse>>> searchAll(@PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostResponse> posts = postService.searchAll(pageable);
        return ResponseEntity.ok().body(new Result<>(posts, posts.getTotalPages()));
    }




    static class Result<T> {
        private T data;
        private int count;
        public Result(T data, int count){
            this.data = data;
            this.count = count;
        }
    }


}
