package az.security.UserAdminManager.controller;

import az.security.UserAdminManager.configration.security.JwtCredentials;
import az.security.UserAdminManager.model.ApiCountCalculate;
import az.security.UserAdminManager.repository.ApiCountCalculateRepository;
import az.security.UserAdminManager.service.ApiCountCalculateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final ApiCountCalculateService apiCountCalculateService;
    private final ApiCountCalculateRepository apiCountCalculateRepository;

    @GetMapping("/hello")
    public String hello() {
        log.info("hello from API security UserAdminManager");
        return "heLLo security UserAdminManager";
    }
    @GetMapping("/user")
    public String getUser() {
        return "User";
    }    @GetMapping("/admin")
    public String getAdmin() {

        JwtCredentials credentials = (JwtCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("user is " + credentials.getSub());
        ApiCountCalculate newEntity = apiCountCalculateService.createNewEntity();
        Optional<ApiCountCalculate> byId = apiCountCalculateRepository.findById(newEntity.getId());
        return "Admin count is :" + byId.get().toString();

    }
}
