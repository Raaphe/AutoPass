package backend.autopass.web;

import backend.autopass.model.entities.User;
import backend.autopass.service.GoogleWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyRole('ADMIN', 'GOOGLE_USER')")
@RequestMapping("/google-wallet-api")
@RequiredArgsConstructor
public class GoogleWalletController {


    private final GoogleWalletService walletService;

    @GetMapping("/add-google-wallet-pass")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Constructs a URL used to add a pass to a user's Google wallet.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "URL sent successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Could not build URL.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403", description = "Pass already exists.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404", description = "User Not Found.",
                    content = @Content
            )
    })
    public ResponseEntity<String> getSavePassURL(@RequestParam Integer userId) {

        try {
            if (walletService.doesPassExist(userId)) {
                return ResponseEntity.status(403).body(null);
            } else {
                return ResponseEntity.ok(walletService.createJWTNewObjects(userId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
