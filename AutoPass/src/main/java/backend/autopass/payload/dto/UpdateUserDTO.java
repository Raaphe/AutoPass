package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateUserDTO - 2024-03-30
 * Raph
 * DTO for Updating user request.
 * AutoPass
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
