package wanted.backend.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wanted.backend.board.entity.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p")
    Page<Post> findWithPagination(Pageable pageable);
}
