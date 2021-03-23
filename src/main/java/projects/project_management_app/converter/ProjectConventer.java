package projects.project_management_app.converter;

import org.springframework.stereotype.Component;
import projects.project_management_app.domain.model.Project;
import projects.project_management_app.web.command.CreateProjectCommand;

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
