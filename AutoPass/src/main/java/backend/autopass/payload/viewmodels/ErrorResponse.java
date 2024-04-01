package backend.autopass.payload.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * ErrorResponse - 2024-03-30
 * Raph
 * Response object for errors.
 * AutoPass
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private Instant timestamp;
    private String message;
    private String path;

    private StackTraceElement[] stackTraceElements;

}
