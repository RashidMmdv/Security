package az.security.UserAdminManager.controller;

import az.security.UserAdminManager.model.ApiCountCalculate;
import az.security.UserAdminManager.service.ApiCountCalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/count")
@RequiredArgsConstructor
public class ApiCountController {

    private final ApiCountCalculateService service;


    @PostMapping("/create")
    public ApiCountCalculate createNewEntity() {
        return service.createNewEntity();
    }
}
