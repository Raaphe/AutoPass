package backend.autopass.web;

import backend.autopass.payload.dto.GetClientStripeSecretDTO;
import backend.autopass.payload.viewmodels.StripeSessionStatusViewModel;
import backend.autopass.service.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * StripeController - 2024-03-30
 * Raph
 * Stripe Client REST controller.
 * Much of the code was taken straight from Stripes <a href="https://docs.stripe.com/checkout/embedded/quickstart">documentation website</a>.
 * AutoPass
 */
@PreAuthorize("hasAnyRole('ADMIN','USER', 'OAUTH2_USER', 'GOOGLE_USER')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class StripeController {
    private final StripeClient stripeClient;

    @GetMapping("/session-status")
    @Operation(summary = "Gets the sessions payment status.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Fetched session status.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StripeSessionStatusViewModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<StripeSessionStatusViewModel> getSessionStatus(String sessionId) {
        try {
            return ResponseEntity.ok(this.stripeClient.getSessionStatus(sessionId));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create-checkout-session")
    @Operation(summary = "Creates a checkout embedded session options.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully created checkout session.",
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
    public ResponseEntity<String> getClientSecret(GetClientStripeSecretDTO dto) {
        try {
            return ResponseEntity.ok(this.stripeClient.getCheckoutOptions(dto.getPriceId()));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
