package az.security.UserAdminManager.service;

import az.security.UserAdminManager.configration.security.BaseJwtService;
import az.security.UserAdminManager.dto.UserDto;
import az.security.UserAdminManager.enums.Role;
import az.security.UserAdminManager.model.Authority;
import az.security.UserAdminManager.model.User;
import az.security.UserAdminManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder  passwordEncoder;
    private final BaseJwtService baseJwtService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    public void createUser(UserDto userDto) {

        userRepository.findByUsername(userDto.getName()).ifPresentOrElse(
                users -> {
                    throw new IllegalArgumentException("Username already exists!");
                },
                ()->{
                    User newUser = User.builder()
                            .username( userDto.getName())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            .authorities(List.of(Authority.builder()
                                            .authority(Role.USER)
                                    .build()))
                            .build();

                    log.info("Creating new user: {}", newUser);

                    userRepository.save(newUser);

                }
        );
    }

    public String login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getName())
                .orElseThrow(() -> new UsernameNotFoundException(userDto.getName()));
        boolean matches = passwordEncoder.matches(userDto.getPassword(), user.getPassword());
        if(!matches){
            throw new IllegalArgumentException("Wrong password!");
        } else {
            return baseJwtService.create(user);
        }
    }

}
