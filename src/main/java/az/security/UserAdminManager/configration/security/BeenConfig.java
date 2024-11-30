package az.security.UserAdminManager.configration.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class BeenConfig {
    @Value("${spring.security.jwt-secret-key}")
    private String jwtSecretKey;
    byte[] keyBytes= Decoders.BASE64.decode(jwtSecretKey);

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtDecoder getJwtDecoder() {
        NimbusJwtDecoder build = NimbusJwtDecoder.withSecretKey(keyBytes.getBytes()).build();
        return build;
    }
}
