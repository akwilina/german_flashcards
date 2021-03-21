package projects.germanflashcards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.germanflashcards.converter.UserConverter;
import projects.germanflashcards.data.user.UserSummary;
import projects.germanflashcards.domain.UserRepository;
import projects.germanflashcards.domain.model.User;
import projects.germanflashcards.domain.model.UserDetails;
import projects.germanflashcards.exception.UserAlreadyExistsException;
import projects.germanflashcards.web.command.RegisterUserCommand;

import javax.transaction.Transactional;
import java.util.Set;

@Service @Transactional @Slf4j @RequiredArgsConstructor
public class UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long create(RegisterUserCommand registerUserCommand) {
        log.debug("User data to be saved: {}", registerUserCommand);

        User userToCreate = userConverter.from(registerUserCommand);
        log.debug("Obtained user object to save {}", userToCreate.getUsername());
        if (userRepository.existsByUsername(userToCreate.getUsername())) {
            log.debug("Registration attempt for existing user");
            throw new UserAlreadyExistsException(String.format("User %s already exist", userToCreate.getUsername()));
        }

        userToCreate.setActive(Boolean.TRUE);
        userToCreate.setRoles(Set.of("ROLE_USER"));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userToCreate.setDetails(UserDetails.builder()
                .user(userToCreate)
                .build());
        userRepository.save(userToCreate);
        log.debug("Saved user: {}", userToCreate);

        return userToCreate.getId();
    }

    public UserSummary getCurrentUserSummaty() {
        log.debug("Obtaining currently logged user details");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.getAuthenticatedUser(username);
        UserSummary summary = userConverter.toUserSummary(user);
        log.debug("Summary data on user {}:", summary);

        return summary;
    }
}
