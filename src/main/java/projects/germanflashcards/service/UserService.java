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
import projects.germanflashcards.web.command.EditUserCommand;
import projects.germanflashcards.web.command.RegisterUserCommand;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(RegisterUserCommand registerUserCommand) {
        log.debug("User data to be saved: {}", registerUserCommand);

        User userToCreate = userConverter.from(registerUserCommand);
        log.debug("Obtained user object to save {}", userToCreate.getUsername());
        if (userRepository.existsByUsername(userToCreate.getUsername())) {
            log.debug("Registration attempt for existing user");
            throw new UserAlreadyExistsException(String.format("User %s already exist", userToCreate.getUsername()));
        }

        setDefaultActive(userToCreate);
        setDefaultData(userToCreate);
        userRepository.save(userToCreate);
        log.debug("Saved user: {}", userToCreate);

        return userToCreate.getId();
    }

    private void setDefaultData(User userToCreate) {
        setDefaultRole(userToCreate);
        setDefaultPassword(userToCreate);
        setDefaultDetails(userToCreate);
    }

    private void setDefaultDetails(User userToCreate) {
        userToCreate.setDetails(UserDetails.builder()
                .user(userToCreate)
                .build());
    }

    private void setDefaultPassword(User userToCreate) {
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
    }

    private void setDefaultRole(User userToCreate) {
        userToCreate.setRoles(Set.of("ROLE_USER"));
    }

    private void setDefaultActive(User userToCreate) {
        userToCreate.setActive(Boolean.TRUE);
    }

    @Transactional
    public UserSummary getCurrentUserSummaty() {
        log.debug("Obtaining currently logged user details");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.getAuthenticatedUser(username);
        UserSummary summary = userConverter.toUserSummary(user);
        log.debug("Summary data on user {}:", summary);

        return summary;
    }

    @Transactional
    public boolean edit(EditUserCommand editUserCommand) {
        log.debug("User data to be edited: {}", editUserCommand);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getAuthenticatedUser(username);
        log.debug("Edit user: {}", user);

        user = userConverter.from(editUserCommand, user);
        log.debug("User data has been edited: {}", user.getDetails());

        return true;
    }
}
