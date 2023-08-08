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
import wanted.backend.board.entity.User;
import wanted.backend.board.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> searchAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return postRepository.findAllByOrderByUserIdDesc(pageRequest).map(PostResponse::from);
    }

    @Transactional
    public void save(PostRequest postRequest, UserAdapter presentUser){
        Post post = postRequest.toEntity(presentUser.getUser());
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse search(Long postId){
        Optional<Post> post = postRepository.findById(postId);
        return post.map(PostDetailResponse::from)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다"));
    }

    @Transactional
    public void update(Long postId, PostRequest postRequest, UserAdapter presentUser){
        Optional<Post> post = postRepository.findById(postId);
        User user = post.map( o -> o.getUser())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다"));
        if(!user.equals(presentUser.getUser())){
            throw new RuntimeException("게시글의 작성자만 수정할 수 있습니다");
        }
        post.get().update(postRequest.getTitle(), postRequest.getContent());
    }

    @Transactional
    public void delete(Long postId, UserAdapter presentUser){
        Optional<Post> post = postRepository.findById(postId);
        User user = post.map( o -> o.getUser())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다"));
        if(!user.equals(presentUser.getUser())){
            throw new RuntimeException("게시글의 작성자만 삭제할 수 있습니다");
        }
        postRepository.deleteById(postId);
    }

}