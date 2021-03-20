package projects.germanflashcards.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.germanflashcards.domain.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
