package backend.autopass.web;


import backend.autopass.model.entities.User;
import backend.autopass.model.enums.Role;
import backend.autopass.payload.dto.*;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.payload.viewmodels.RefreshTokenResponse;
import backend.autopass.security.jwt.refreshToken.Token;
import backend.autopass.service.AuthenticationService;
import backend.autopass.service.JwtService;
import backend.autopass.service.RefreshTokenService;
import backend.autopass.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like login, logout, refresh-token etc.")
@RequestMapping("/auth")
@SecurityRequirements()
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody SignInDTO request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        if (authenticationResponse == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
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
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody SignUpDTO request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        if (authenticationResponse == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
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
    public ResponseEntity<String> refreshAccessToken(@RequestParam String refreshToken) {
        Optional<Token> token = refreshTokenService.findTokenByToken(refreshToken);
        String accessToken;

        if (token.isPresent()) {
            boolean isExpired = refreshTokenService.isTokenExpired(token.get());
            Optional<User> user = refreshTokenService.findUserByToken(refreshToken);

            if (isExpired) {
                user.ifPresent(value -> refreshTokenService.deleteByUserId((long) value.getId()));
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            RefreshTokenResponse tokenResponse = refreshTokenService.generateNewToken(new RefreshTokenDTO(refreshToken));
            accessToken = tokenResponse.getAccessToken();

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
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
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        refreshTokenService.deleteByUserId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/app-ip")
    @Operation(summary = "Gets the backend's Ip, which is set in application properties.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully fetched."
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<String> getAppIp() {
        return ResponseEntity.ok(authenticationService.getAppIp());
    }

    @PostMapping("/isLogged")
    @Operation(summary = "Checks if the access token is valid.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Access token is valid, resources are permitted. Returns true or false.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<Boolean> isLogged(@RequestBody IsLoggedInDTO dto) {

        // User doesn't exist
        if (dto.getAccessToken().isBlank() || dto.getUserId() < 0 || String.valueOf(dto.getUserId()).equals("-1")) {
            System.out.println("isLogged -> Empty DTO");
            return ResponseEntity.ok()
                    .body(false);
        }

        // ===== ACCESS TOKEN ====
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.jwtService.extractUserName(dto.getAccessToken()));
            if (!jwtService.isTokenValid(dto.getAccessToken(), userDetails)) {
                return ResponseEntity.ok().body(false);
            } else {
                return ResponseEntity.ok().body(true);
            }
        } catch (ExpiredJwtException je) {
            return ok().body(false);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/check-refresh-token")
    @Operation(summary = "Checks if the refresh token is valid.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Token is valid and user's access token is refreshed.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Invalid tokens",
                    content = @Content
            )
    })
    public ResponseEntity<Boolean> isRefreshTokenExpired(@RequestBody RefreshTokenDTO dto) {
        try {
            Optional<Token> refreshToken = refreshTokenService.findTokenByToken(dto.getRefreshToken());
            if (refreshToken.isEmpty()) {
                return ok().body(true);
            }
            if (refreshTokenService.isTokenExpired(refreshToken.get())) {
                return ok().body(true);
            } else {
                return ok().body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Sends an email with link to reset password.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Link was successfully sent.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<Boolean> forgotPassword(@RequestParam String email) {
        try {
            return ResponseEntity.ok(authenticationService.forgotPassword(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @Operation(summary = "Changes a user's password.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Password successfully changed",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content
            )
    })
    @PostMapping("/update-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody @Valid ChangePasswordDTO passwordDTO) {
        try {
            return ResponseEntity.ok().body(this.userService.changePassword(passwordDTO));
        } catch (Exception e) {
            return ResponseEntity.ok().body(false);
        }
    }

    @Operation(summary = "Gets a user's role from access token.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Role Successfully fetched.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Role.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content
            )
    })
    @GetMapping("/get-user-role")
    public ResponseEntity<Role> getUserRole(@RequestParam String accessToken) {
        try {
            return ResponseEntity.ok(authenticationService.getRoleFromAccessToken(accessToken));
        } catch (Exception e) {
            return ResponseEntity.ok().body(null);
        }
    }
}
