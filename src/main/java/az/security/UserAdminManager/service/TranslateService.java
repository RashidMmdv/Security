package az.security.UserAdminManager.service;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslateService {
    private final MessageSource messageSource;

    public String translate(String code, String lang, String... args) {
        return messageSource.getMessage(code, args, Locale.of(lang));
    }
}
