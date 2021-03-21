package projects.germanflashcards.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.germanflashcards.service.ProjectService;
import projects.germanflashcards.web.command.CreateProjectCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/projects")
@Slf4j
@RequiredArgsConstructor
public class AddNewProjectController {

    private final ProjectService projectService;

    @GetMapping("/add")
    public String getAddProjectPage(Model model) {
        model.addAttribute(new CreateProjectCommand());
        return "project/add";
    }

    @PostMapping("/add")
    public String processAddProject(@Valid CreateProjectCommand createProjectCommand, BindingResult bindings) {
        log.debug("Data for project creation: {}", createProjectCommand);

        if (bindings.hasErrors()) {
            log.debug("Data has errors: {}", bindings.getAllErrors());
            return "project/add";
        }

        try {
            projectService.add(createProjectCommand);
            log.debug("Project has been created");
            return "redirect:/projects";

        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Error while project creation", re);
            bindings.rejectValue(null, null, "Error occured");
            return "project/add";
        }
    }
}
