package backend.autopass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * TokenException - 2024-03-30
 * Raph
 * Token exception for JWTs.
 * AutoPass
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenException extends RuntimeException {
    public TokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
