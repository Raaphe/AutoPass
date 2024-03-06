package backend.autopass.web;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
@SecurityRequirements()
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9090"})
@RequiredArgsConstructor
@RestController
public class StripeController {

    @Value("${server.port}")
    private String yourDomain;

    @PostMapping("/create-checkout-session")
    public RedirectView createSession() throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail("customer@example.com")
                .setSubmitType(SessionCreateParams.SubmitType.PAY)
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(yourDomain + "/success.html")
                .setCancelUrl(yourDomain + "/cancel.html")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice("price_1OozstHOJBVORZPIM10as4x5")
                                .build())
                .build();

        Session session = Session.create(params);
        return new RedirectView(session.getUrl(), true);
    }
}
