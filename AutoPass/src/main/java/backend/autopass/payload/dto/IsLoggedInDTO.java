package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * IsLoggedInDTO - 2024-03-30
 * Raph
 * DTO for the request checking if the user is logged in.
 * AutoPass
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsLoggedInDTO {
    private String accessToken;
    private Long userId;
}
