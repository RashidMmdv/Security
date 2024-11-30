package az.security.UserAdminManager.service;

import az.security.UserAdminManager.configration.security.JwtAuthService;
import az.security.UserAdminManager.model.ApiCountCalculate;
import az.security.UserAdminManager.repository.ApiCountCalculateRepository;
import az.security.UserAdminManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiCountCalculateService {

    private final ApiCountCalculateRepository apiCountCalculateRepository;
    private final JwtAuthService jwtAuthService;
    private final JwtDecoder jwtDecoder;

    @Transactional
    public ApiCountCalculate createNewEntity() {

        Long maxCount = apiCountCalculateRepository.findMaxCount();
        if (maxCount == null) {
            maxCount = 0L;
        }
        String authentication = jwtAuthService.getAuthentication();
        String subject = jwtDecoder.decode(authentication).getSubject();
        log.info("Subject: " +subject);
        ApiCountCalculate newEntity = ApiCountCalculate.builder()
                .count(maxCount + 1)
                .localDateTime(LocalDateTime.now())
                .token(authentication)
                .build();

        return apiCountCalculateRepository.save(newEntity);
    }

}
