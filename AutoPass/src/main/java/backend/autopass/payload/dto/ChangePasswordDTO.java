package backend.autopass.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangePasswordDTO - 2024-03-30
 * Raph
 * DTO for change password request.
 * AutoPass
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank
    String token;
    @NotBlank
    String password;
}
