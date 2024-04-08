package backend.autopass.service.interfaces;


import backend.autopass.payload.viewmodels.StripeSessionStatusViewModel;
import com.stripe.exception.StripeException;

/**
 * IStripeClient - 2024-04-06
 * Raph
 * The interface for stripe client methods.
 * AutoPass
 */
public interface IStripeClient {

    /**
     * Returns the session ID that the frontend will consume to create an embedded checkout form.
     *
     * @param priceId The Stripe price ID for a given product.
     * @return The checkout session options.
     */
    String getCheckoutOptions(String priceId) throws StripeException;

    /**
     * Gets a session status for the return page.
     * @param sessionId The session ID.
     * @return The view model for the return page.
     */
    StripeSessionStatusViewModel getSessionStatus(String sessionId) throws StripeException;
}
