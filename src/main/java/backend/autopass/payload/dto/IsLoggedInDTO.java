package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IsLoggedInDTO extends RefreshTokenDTO {
    private String accessToken;
}
