package backend.autopass.web;

import backend.autopass.model.entities.UserWallet;
import backend.autopass.service.UserWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * WalletController - 2024-03-30
 * Raph
 * User Wallet REST Controller.
 * AutoPass
 */
@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GOOGLE_USER')")
@RequiredArgsConstructor
@RequestMapping("/wallet")
@Slf4j
public class WalletController {

    private final UserWalletService userWalletService;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/wallet-info")
    @Operation(summary = "Gets a user's wallet information by their ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully provided the wallet information.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserWallet.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<UserWallet> getUserWalletByUserId(@RequestParam int userId) {
        try {
            UserWallet userWallet = userWalletService.getUserWalletInfo(userId);
            assert userWallet != null;
            return ResponseEntity.ok(userWallet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserWallet());
        }
    }

}
