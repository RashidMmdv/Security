package az.security.UserAdminManager.configration.security;

import az.security.UserAdminManager.exception.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static az.security.UserAdminManager.exception.ErrorCode.ACCESS_TOKEN_EXPIRED;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    private final List<AuthService> authServices;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException {
        try {
            Optional<Authentication> authOptional = Optional.empty();
            for (AuthService authService : authServices) {
                authOptional = authOptional.or(() -> authService.getAuthentication(httpServletRequest));
            }
            authOptional.ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            switch (e) {
                case ExpiredJwtException expiredJwtException ->
                        handleJwtExpiredException(httpServletRequest, httpServletResponse);
                default -> throw new JwtException("Invalid token");
            }

        }
    }

    private void handleJwtExpiredException(HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse)
            throws IOException {
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder()
                .data(new HashMap<>())
                .message("Expired")
                .path(httpServletRequest.getRequestURI())
                .requestedLang(httpServletRequest.getHeader("Accept-Language"))
                .code(ACCESS_TOKEN_EXPIRED)
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .detail("Access token expired.")
                .build();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonErrorResponse = new ObjectMapper().writeValueAsString(errorResponseDTO);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(jsonErrorResponse);
    }


}
