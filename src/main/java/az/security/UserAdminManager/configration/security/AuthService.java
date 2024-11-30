package az.security.UserAdminManager.configration.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest);

}
