package backend.autopass.payload.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AuthenticationResponse - 2024-03-30
 * Raph
 * Response object for authentication request.
 * AutoPass
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("user_id")
    private Long id;
    @JsonProperty("user_email")
    private String email;
    @JsonProperty("user_authorities")
    private List<String> roles;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;

}
