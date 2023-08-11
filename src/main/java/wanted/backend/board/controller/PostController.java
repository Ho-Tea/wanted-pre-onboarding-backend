package wanted.backend.board.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wanted.backend.board.dto.*;
import wanted.backend.board.service.PostService;
import wanted.backend.board.vo.PageInfo;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/searchAll")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity searchAll(@Positive @RequestParam int page, @Positive @RequestParam int size) {
        Page<PostResponse> posts = postService.searchAll(page, size);
        PageInfo pageInfo = new PageInfo(size, page, (int) posts.getTotalElements(), posts.getTotalPages());
        return new ResponseEntity<>(new PostListDto(posts.getContent(), pageInfo), HttpStatus.OK);
    }


    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> save(@Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserAdapter presentUser) {
        postService.save(postRequest, presentUser);
        return ResponseEntity.ok("새로운 게시글이 생성되었습니다");
    }


    @GetMapping("/search/{postId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<PostDetailResponse> search(@PathVariable Long postId) {
        PostDetailResponse postDetailResponse = postService.search(postId);
        return ResponseEntity.ok(postDetailResponse);
    }


    @PostMapping("/update/{postId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> update(@PathVariable Long postId,
                                         @Valid @RequestBody PostRequest postRequest,
                                         @AuthenticationPrincipal UserAdapter presentUser) {
        postService.update(postId, postRequest, presentUser);
        return ResponseEntity.ok("게시글이 수정되었습니다");
    }

    @PostMapping("/delete/{postId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> delete(@PathVariable Long postId,
                                         @AuthenticationPrincipal UserAdapter presentUser) {
        postService.delete(postId, presentUser);
        return ResponseEntity.ok("게시글이 삭제되었습니다");
    }


}