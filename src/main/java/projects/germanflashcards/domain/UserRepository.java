package projects.germanflashcards.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.germanflashcards.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
