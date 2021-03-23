package projects.project_management_app.converter;

import org.springframework.stereotype.Component;
import projects.project_management_app.data.user.UserSummary;
import projects.project_management_app.domain.model.User;
import projects.project_management_app.domain.model.UserDetails;
import projects.project_management_app.web.command.EditUserCommand;
import projects.project_management_app.web.command.RegisterUserCommand;

@Component
public class UserConverter {


    public User from(RegisterUserCommand registerUserCommand) {
        return User.builder()
                .username(registerUserCommand.getUsername())
                .password(registerUserCommand.getPassword())
                .build();
    }

    public UserSummary toUserSummary(User user) {
        UserDetails details = user.getDetails();

        return UserSummary.builder()
                .username(user.getUsername())
                .firstName(details.getFirstName())
                .lastName(details.getLastName())
                .birthDate(details.getBirthDate())
                .build();
    }

    public User from(EditUserCommand editUserCommand, User user) {
        UserDetails details = user.getDetails();
        details.setFirstName(editUserCommand.getFirstName());
        details.setLastName(editUserCommand.getLastName());
        details.setBirthDate(editUserCommand.getBirthDate());

        return user;
    }
}
