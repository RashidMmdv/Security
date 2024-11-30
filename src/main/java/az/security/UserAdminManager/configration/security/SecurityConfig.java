package az.security.UserAdminManager.configration.security;

import az.security.UserAdminManager.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthRequestFilter authRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/test/hello").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/count/**").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/test/user").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name()));
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/test/admin").hasAnyAuthority(Role.ADMIN.name()));
        http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
