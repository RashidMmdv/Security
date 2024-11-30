package az.security.UserAdminManager.configration.security;

import az.security.UserAdminManager.model.Authority;
import az.security.UserAdminManager.model.User;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class BaseJwtService {
    private Key key;

    @Value("${spring.security.jwt-secret-key}")
    private String jwtSecretKey;

    @PostConstruct
    public void init(){
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

public Jws<Claims> parseToken(String token) {
    return Jwts.parser()
            .verifyWith((SecretKey) key)
            .build()
            .parseSignedClaims(token);
}


    public String create(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
                .header()
                .add("tokenType", "access")
                .and()
                .claim("authority", user.getAuthorities().stream().map(Authority::getAuthority).toList())
                .signWith(key)
                .compact();
    }



}
