package wanted.backend.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.backend.board.dto.PostDto;
import wanted.backend.board.entity.Post;
import wanted.backend.board.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostDto> searchAll(Pageable pageable){
        return postRepository.findWithPagination(pageable).map(PostDto::from);
    }
}
