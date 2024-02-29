package backend.autopass.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    SessionCreateParams params =
            SessionCreateParams.builder()
                    .setSuccessUrl("https://example.com/success")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice("price_1MotwRLkdIwHu7ixYcPLm5uZ")
                                    .setQuantity(2L)
                                    .build()
                    )
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .build();

    Session session;
    {
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
