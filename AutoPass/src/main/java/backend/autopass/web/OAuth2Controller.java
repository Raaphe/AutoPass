package backend.autopass.web;

import backend.autopass.model.entities.User;
import backend.autopass.service.Oauth2UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * OAuth2Controller - 2024-03-30
 * Raph
 * Oauth2 REST Controller for Google from AutoPass.
 * AutoPass
 */
@RestController
@SecurityRequirements()
@RequiredArgsConstructor
public class OAuth2Controller {

    private final Oauth2UserService userService;

    @GetMapping("/google/user")
    @Operation(summary = "Gets a user's information by their ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<User> getUser(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok().body(user);
        }
    }


}
