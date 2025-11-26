package wat.grupa.trzy.wielkieakcjeitransakcje;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomLogoutHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> errorResponse = Map.of(
                "message", ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        log.error("Wystąpił nieoczekiwany błąd aplikacji", ex);

        Map<String, String> errorResponse = Map.of(
                "error", "Wystąpił wewnętrzny błąd serwera.",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
