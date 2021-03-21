package projects.germanflashcards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projects.germanflashcards.converter.ProjectConventer;
import projects.germanflashcards.domain.UserRepository;
import projects.germanflashcards.domain.model.Project;
import projects.germanflashcards.domain.model.User;
import projects.germanflashcards.domain.repository.ProjectRepository;
import projects.germanflashcards.web.command.CreateProjectCommand;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectConventer projectConventer;

    @Transactional
    public void add(CreateProjectCommand createProjectCommand) {
        log.debug("Data for project creation: {}", createProjectCommand);

        Project project = projectConventer.from(createProjectCommand);
        updateProjectWithUser(project);
        log.debug("Project to be saved: {}", project);

        projectRepository.save(project);
        log.debug("Project has been saved: {}", project);
    }

    private void updateProjectWithUser(Project project) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getAuthenticatedUser(username);
        project.setUser(user);
    }


}
