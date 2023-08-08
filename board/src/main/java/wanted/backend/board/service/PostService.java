package wanted.backend.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.backend.board.dto.PostDetailResponse;
import wanted.backend.board.dto.PostRequest;
import wanted.backend.board.dto.PostResponse;
import wanted.backend.board.dto.UserAdapter;
import wanted.backend.board.entity.Post;
import wanted.backend.board.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> searchAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return postRepository.findAllByOrderByUserIdDesc(pageRequest).map(PostResponse::from);
    }

    public void save(PostRequest postRequest, UserAdapter userAdapter){
        Post post = postRequest.toEntity(userAdapter.getUser());
        postRepository.save(post);
    }

    public PostDetailResponse search(Long postId){
        Optional<Post> post = postRepository.findById(postId);
        return post.map(PostDetailResponse::from)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID"));
    }
}