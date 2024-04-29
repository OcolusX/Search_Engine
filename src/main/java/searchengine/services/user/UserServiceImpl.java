package searchengine.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import searchengine.exception.UserException;
import searchengine.model.Role;
import searchengine.model.User;
import searchengine.repository.RoleRepository;
import searchengine.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User create(User user) throws UserException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("Пользователь с таким адресом электронной почты уже существует");
        }

        return userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) throws UserException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public Role getRole(String roleName) throws UserException {
        return roleRepository.findByName(roleName).orElseThrow(() -> new UserException("Роль не найдена"));
    }

    @Override
    public User getCurrentUser() throws UserException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
