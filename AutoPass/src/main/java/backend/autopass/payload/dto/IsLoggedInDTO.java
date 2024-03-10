package backend.autopass.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsLoggedInDTO {
    private String accessToken;
    private Long userId;
}
