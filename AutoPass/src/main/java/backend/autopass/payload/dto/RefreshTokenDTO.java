package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RefreshTokenDTO - 2024-03-30
 * Raph
 * DTO for refreshing request.
 * AutoPass
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {
    private String refreshToken;
}
