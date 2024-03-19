package backend.autopass.web;

import backend.autopass.service.StripeClient;
import com.stripe.model.Charge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasAnyRole('ADMIN','USER', 'OAUTH2_USER', 'GOOGLE_USER')")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/payment")
public class StripeController {
//    private final StripeClient stripeClient;
//
//    @PostMapping("/charge")
//    @Operation(summary = "Charges a card givent a token and an amount.")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200", description = "Successfully processed the payment.",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = Charge.class))
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "400", description = "Bad Request.",
//                    content = @Content
//            )
//    })
//    public Charge chargeCard(@RequestHeader(value = "token") String token, @RequestHeader(value = "amount") Double amount) throws Exception {
//        return this.stripeClient.chargeNewCard(token, amount);
//    }
}
