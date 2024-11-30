package az.security.UserAdminManager.exception;
import az.security.UserAdminManager.exception.*;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private ErrorCode code;
    private String[] args;

    public NotFoundException(ErrorCode code, String... args) {
        this.code = code;
        this.args = args;
    }
}
