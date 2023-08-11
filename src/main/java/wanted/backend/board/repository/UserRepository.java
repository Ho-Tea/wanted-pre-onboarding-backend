package wanted.backend.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.backend.board.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
