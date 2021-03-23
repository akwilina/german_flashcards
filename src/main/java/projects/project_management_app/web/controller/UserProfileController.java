package projects.project_management_app.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.project_management_app.data.user.UserSummary;
import projects.project_management_app.service.UserService;
import projects.project_management_app.web.command.EditUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @ModelAttribute
    public UserSummary userSummary() {
        return userService.getCurrentUserSummaty();
    }

    @GetMapping
    public String getProfilePage(Model model) {
        EditUserCommand editUserCommand = createEditUserCommand(userSummary());
        model.addAttribute(editUserCommand);
        return "user/profile";
    }

    private EditUserCommand createEditUserCommand(UserSummary summary) {
        return EditUserCommand.builder()
                .firstName(summary.getFirstName())
                .lastName(summary.getLastName())
                .birthDate(summary.getBirthDate())
                .build();
    }

    @PostMapping("/edit")
    public String editUserProfile(@Valid EditUserCommand editUserCommand, BindingResult bindings) {
        log.debug("Data to edit user {}", editUserCommand);

        if (bindings.hasErrors()) {
            log.debug("Wrong data: {}", bindings.getAllErrors());
            return "user/profile";
        }

        try {
            boolean success = userService.edit(editUserCommand);
            log.debug("Data edition successed? {}", success);
            return "redirect:/profile";

        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Error in data edition", re);
            bindings.rejectValue(null, null, "Error has occured");

        }

        return "redirect:/profile";

    }

}
