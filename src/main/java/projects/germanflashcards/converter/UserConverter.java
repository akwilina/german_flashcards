package projects.germanflashcards.converter;

import org.springframework.stereotype.Component;
import projects.germanflashcards.domain.model.User;
import projects.germanflashcards.web.command.RegisterUserCommand;

@Component
public class UserConverter {


    public User from(RegisterUserCommand registerUserCommand) {
        return User.builder()
                .username(registerUserCommand.getUsername())
                .password(registerUserCommand.getPassword())
                .build();
    }
}
