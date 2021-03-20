package projects.germanflashcards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projects.germanflashcards.web.command.RegisterUserCommand;
import javax.transaction.Transactional;

@Service @Transactional @Slf4j @RequiredArgsConstructor
public class UserService {

    public Long create(RegisterUserCommand registerUserCommand) {
        return null;
    }
}
