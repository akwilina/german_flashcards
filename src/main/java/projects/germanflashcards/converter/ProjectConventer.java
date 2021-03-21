package projects.germanflashcards.converter;

import org.springframework.stereotype.Component;
import projects.germanflashcards.domain.model.Project;
import projects.germanflashcards.web.command.CreateProjectCommand;

@Component
public class ProjectConventer {
    public Project from(CreateProjectCommand command) {
        return Project.builder()
                .name(command.getName())
                .url(command.getUrl())
                .description(command.getDescription())
                .build();
    }
}
