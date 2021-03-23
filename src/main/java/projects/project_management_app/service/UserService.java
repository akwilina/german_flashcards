package projects.project_management_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.project_management_app.converter.UserConverter;
import projects.project_management_app.data.user.UserSummary;
import projects.project_management_app.domain.UserRepository;
import projects.project_management_app.domain.model.User;
import projects.project_management_app.domain.model.UserDetails;
import projects.project_management_app.exception.UserAlreadyExistsException;
import projects.project_management_app.web.command.EditUserCommand;
import projects.project_management_app.web.command.RegisterUserCommand;

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
