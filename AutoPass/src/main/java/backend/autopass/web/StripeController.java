package backend.autopass.web;


import backend.autopass.service.StripeClient;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@PreAuthorize("hasAnyRole('ADMIN','USER', 'OAUTH2_USER', 'GOOGLE_USER')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class StripeController {
    private final StripeClient stripeClient;

    @PostMapping("/charge")
    public Charge chargeCard(@RequestHeader(value = "token") String token, @RequestHeader(value = "amount") Double amount) throws Exception {
        return this.stripeClient.chargeNewCard(token, amount);
    }
}
