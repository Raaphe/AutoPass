package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SignInDTO - 2024-03-30
 * Raph
 * DTO for logging in request.
 * AutoPass
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInDTO {
    String email;
    String password;
}

