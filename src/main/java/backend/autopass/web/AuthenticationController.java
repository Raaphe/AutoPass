package backend.autopass.web;

import backend.autopass.payload.dto.RefreshTokenDTO;
import backend.autopass.payload.dto.SignInDTO;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.service.AuthenticationService;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like login, logout, refresh-token etc.")
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
@RequestMapping("/auth")
@SecurityRequirements()
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Logs a user in by an email and password. Returns a JWT token for session handling.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful login.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RefreshTokenResponse.class))
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
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authenticationResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(authenticationResponse);
    }

    @PostMapping("/signup")
    @Operation(summary = "Upon successful registration, it returns JWT and refresh token cookies, similar to the login process. This allows the user to immediately log in after signing up.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful signup, access token attached to response.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RefreshTokenResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request. Invalid signup details.",
                    content = @Content
            )
    })
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody SignUpDTO request) throws Exception {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authenticationResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token-cookie")
    @Operation(summary = "Allows users to refresh their JWT using a cookie that contains the refresh token. It extracts the refresh token from the cookie, generates a new JWT, and sends it back in a new cookie.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful signup, access token attached to response."
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> refreshTokenCookie(HttpServletRequest request) {

        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(request);

        RefreshTokenResponse refreshTokenResponse = refreshTokenService.generateNewToken(new RefreshTokenDTO(refreshToken));

        ResponseCookie NewJwtCookie = jwtService.generateJwtCookie(refreshTokenResponse.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, NewJwtCookie.toString())
                .build();
    }

    @PostMapping("/info")
    @Operation(summary = "A method to retrieve authentication information for a user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Retrieves the user's login state details.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Authentication.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request. Invalid user details.",
                    content = @Content
            )
    })
    public Authentication getAuthentication(@RequestBody SignInDTO request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/logout")
    @Operation(summary = "This method handles user logout. It removes the refresh token associated with the user (if it exists) and clears the JWT and refresh token cookies, effectively logging the user out.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User tokens are cleared and refreshed."
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(request);
        if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }
        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();

    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Generates a new access token when the current one is expired but the refresh token in valid and sends it back in the response.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully generated new access session token.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RefreshTokenResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDTO request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }


}
