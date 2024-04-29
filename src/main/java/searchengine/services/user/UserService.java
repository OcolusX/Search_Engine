package searchengine.services.user;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import searchengine.model.Role;
import searchengine.model.User;

@Service
public interface UserService {

    User create(User user);

    User getByUsername(String username);

    Role getRole(String roleName);

    UserDetailsService userDetailsService();

    User getCurrentUser();
}
