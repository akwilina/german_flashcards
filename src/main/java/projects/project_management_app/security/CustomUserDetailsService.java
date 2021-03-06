package projects.project_management_app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import projects.project_management_app.domain.UserRepository;

import java.util.stream.Collectors;

@Component
@Slf4j @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Obtaining user by username:{}", username);
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getActive(),
                        true,
                        true,
                        true,
                        user.getRoles().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet())))
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }
}