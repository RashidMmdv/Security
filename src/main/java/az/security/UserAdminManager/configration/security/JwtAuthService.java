package az.security.UserAdminManager.configration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthService implements AuthService{

    private final BaseJwtService baseJwtService;
    String token;

    @Override
    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);


            Jws<Claims> jws = baseJwtService.parseToken(token);
            return Optional.of(getAuthentication(jws.getPayload()));
        }

        return Optional.empty();
    }
    public String getAuthentication() {
        log.info(token);
        return token;
    }


    private Authentication getAuthentication(Claims claims) {
        List<String> roles = (List) claims.get("authority");
        List<GrantedAuthority> authorityList = roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtCredentials credentials = new ModelMapper().map(claims, JwtCredentials.class);

        return new UsernamePasswordAuthenticationToken(credentials, null, authorityList);
    }
}
