package backend.autopass.web;

//import backend.autopass.payload.dto.IsLoggedInDTO;

import backend.autopass.payload.dto.IsLoggedInDTO;
import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import backend.autopass.service.AuthenticationService;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import backend.autopass.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like login, logout, refresh-token etc.")
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
@RequestMapping("/auth")
@SecurityRequirements()
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Logs a user in by an email and password. Returns a JWT token for session handling.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful login.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized. Invalid login credentials.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403", description = "Server refused to respond. Unauthorized.",
                    content = @Content
            )
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody SignInDTO request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ok()
                .body(authenticationResponse);
    }

    @PostMapping("/signup")
    @Operation(summary = "Upon successful registration, it returns JWT and refresh token cookies, similar to the login process. This allows the user to immediately log in after signing up.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful signup, access token attached to response.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request. Invalid signup details.",
                    content = @Content
            )
    })
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody SignUpDTO request) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        return ok()
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Used to refresh expired access token. Verifies if refresh token is valid.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful signup, a new access token is returned to frontend.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<String> refreshTokenCookie(String refreshToken) {

        RefreshTokenResponse refreshTokenResponse = refreshTokenService.generateNewToken(new RefreshTokenDTO(refreshToken));

        ResponseCookie newAccessToken = jwtService.generateJwtCookie(refreshTokenResponse.getAccessToken());

        return ok()
                .body(newAccessToken.toString());
    }

    @PostMapping("/logout")
    @Operation(summary = "This method handles user logout. It removes the refresh token associated with the user (if it exists).")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Refresh token is deleted"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return ok()
                .build();
    }

    @PostMapping("/isLogged")
    @Operation(summary = "Checks if the jwt tokens are valid.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Tokens are valid and user is logged in."
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid tokens",
                    content = @Content
            )
    })
    public ResponseEntity<Boolean> isLogged(IsLoggedInDTO dto) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                this.userService.getUserById((long) dto.getUserId()).getEmail() // email
        );


        // ===== REFRESH TOKEN ====
        Optional<Token> refreshToken = refreshTokenService.findTokenByToken(dto.getRefreshToken());
        if (refreshToken.isPresent()) {

            try {
                refreshTokenService.verifyExpiration(refreshToken.get());
            } catch (Exception exception) {
                return ok().body(false);
            }

        }

        // ===== ACCESS TOKEN ====
        if (!jwtService.isTokenValid(dto.getAccessToken(), userDetails)) {
            refreshTokenService.deleteByToken(dto.getRefreshToken());
            return ok().body(false);
        }

        return ok()
                .build();
    }
}
