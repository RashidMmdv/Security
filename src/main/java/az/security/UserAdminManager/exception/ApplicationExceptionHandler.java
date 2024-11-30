package az.security.UserAdminManager.exception;


import static az.security.UserAdminManager.exception.ErrorCode.GENERIC_RUNTIME_EXCEPTION;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import az.security.UserAdminManager.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

    private final TranslateService translateService;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException
            (RuntimeException ex, WebRequest request) {
        String language = request.getHeader("Accept-Language");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .code(GENERIC_RUNTIME_EXCEPTION)
                        .message(ex.getMessage())
                        .detail(ex.getMessage())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .requestedLang(language)
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleStudentNotFoundException
            (NotFoundException ex, WebRequest request) {
        String language = request.getHeader("Accept-Language");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .code(ex.getCode())
                        .message(translateService.translate(ex.getCode().getName(), language, ex.getArgs()))
                        .detail(translateService.translate(ex.getCode().getName().concat("_DETAIL"), language, ex.getArgs()))
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .requestedLang(language)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorResponseDto> handle(HttpMessageNotReadableException ex, WebRequest request) {
        ex.printStackTrace();
        String language = request.getHeader("Accept-Language");

        ErrorResponseDto response = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(GENERIC_RUNTIME_EXCEPTION)
                .message("Bad request")
                .detail(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .requestedLang(language)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponseDto> handle(MethodArgumentNotValidException ex, WebRequest request) {
        ex.printStackTrace();
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        String language = request.getHeader("Accept-Language");

        ErrorResponseDto response = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(GENERIC_RUNTIME_EXCEPTION)
                .message("Bad request")
                .detail(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .requestedLang(language)
                .build();
        validationErrors.forEach(
                validation -> response.getData().put(validation.getProperty(), validation.getMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
