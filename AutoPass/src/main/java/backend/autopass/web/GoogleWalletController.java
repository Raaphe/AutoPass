package backend.autopass.web;

import backend.autopass.payload.viewmodels.GoogleWalletPassURLViewModel;
import backend.autopass.service.GoogleWalletService;
import backend.autopass.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * GoogleWalletController - 2024-03-30
 * Raph
 * Google Wallet AutoPass REST Controller.
 * AutoPass
 */
@PreAuthorize("hasAnyRole('ADMIN', 'GOOGLE_USER')")
@RequestMapping("/google-wallet-api")
@RequiredArgsConstructor
@RestController
public class GoogleWalletController {

    private final GoogleWalletService walletService;

    @GetMapping("/get-add-pass-url")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Constructs a URL used to add a pass to a user's Google wallet.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "URL sent successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GoogleWalletPassURLViewModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Could not build URL.",
                    content = @Content
            ),
    })
    public ResponseEntity<GoogleWalletPassURLViewModel> getSavePassURL(@RequestParam Integer userId) {
        try {
            return ResponseEntity.ok(walletService.createObject(userId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/expire-pass")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Sets a user's pass to expired state.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Pass expired.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            ),
    })
    public ResponseEntity<Boolean> expirePass(@RequestParam String email) {
        try {
            return ResponseEntity.ok(walletService.expireObject(email.replace("@", "."), email));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/activate-pass")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Sets a user's pass to active state.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Pass activated.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            ),
    })
    public ResponseEntity<Boolean> activatePass(@RequestParam String email) {
        try {
            walletService.getJwtForPass(email, true);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
