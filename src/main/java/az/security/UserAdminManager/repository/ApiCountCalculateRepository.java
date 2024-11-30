package az.security.UserAdminManager.repository;

import az.security.UserAdminManager.model.ApiCountCalculate;
import az.security.UserAdminManager.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ApiCountCalculateRepository extends JpaRepository<ApiCountCalculate, Long> {



    @Query("SELECT MAX(a.count) FROM ApiCountCalculate a")
    Long findMaxCount();
}
