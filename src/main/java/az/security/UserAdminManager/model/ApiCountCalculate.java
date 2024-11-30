package az.security.UserAdminManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ApiCountCalculate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long count = 0L;
    LocalDateTime localDateTime = LocalDateTime.now();
    String token;

}
