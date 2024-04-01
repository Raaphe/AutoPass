package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthenticationDTO - 2024-03-30
 * Raph
 * DTO for authentication request.
 * AutoPass
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {

    String password;
    private String email;
}
