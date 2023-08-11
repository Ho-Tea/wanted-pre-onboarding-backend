package wanted.backend.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.backend.board.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByUserIdDesc(Pageable pageable);
}
