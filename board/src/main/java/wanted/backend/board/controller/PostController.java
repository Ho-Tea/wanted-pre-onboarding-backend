package wanted.backend.board.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wanted.backend.board.dto.PostListDto;
import wanted.backend.board.dto.PostRequest;
import wanted.backend.board.dto.PostResponse;
import wanted.backend.board.dto.UserAdapter;
import wanted.backend.board.repository.PostRepository;
import wanted.backend.board.service.PostService;
import wanted.backend.board.vo.PageInfo;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/searchAll")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity searchAll(@Positive @RequestParam int page, @Positive @RequestParam int size ){
        Page<PostResponse> posts = postService.searchAll(page, size);
        PageInfo pageInfo = new PageInfo(size, page, (int) posts.getTotalElements(), posts.getTotalPages());
        return new ResponseEntity<>(new PostListDto(posts.getContent(), pageInfo), HttpStatus.OK);
    }


    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> save(@Valid @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserAdapter userAdapter){
        postService.save(postRequest, userAdapter);
        return ResponseEntity.ok("저장되었습니다");
    }


}