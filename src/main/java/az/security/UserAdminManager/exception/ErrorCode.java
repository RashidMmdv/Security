package az.security.UserAdminManager.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND_0001("ENTITY_NOT_FOUND_0001"),
    GENERIC_RUNTIME_EXCEPTION("GENERIC_RUNTIME_EXCEPTION"),
    ACCESS_TOKEN_EXPIRED("ACCESS_TOKEN_EXPIRED_EXCEPTION")
    ;

    private final String name;

    ErrorCode(String name) {
        this.name = name;
    }
}
