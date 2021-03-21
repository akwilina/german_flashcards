package projects.germanflashcards.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.germanflashcards.domain.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
