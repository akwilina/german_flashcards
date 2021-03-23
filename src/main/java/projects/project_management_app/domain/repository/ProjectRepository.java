package projects.project_management_app.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.project_management_app.domain.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
