package backend.autopass.web;

import backend.autopass.model.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@RestController
public class SubscriptionController {

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession() throws Exception {
        String DOMAIN = "http://localhost:9090";
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setSuccessUrl(DOMAIN + "/success")
                        .setCancelUrl(DOMAIN + "/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPrice("price_1OozstHOJBVORZPIM10as4x5")
                                        .build())
                        .build();
        Session session = Session.create(params);
        return "redirect:" + session.getUrl();
    }

}
