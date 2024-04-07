package backend.autopass.payload.viewmodels;

import lombok.Builder;
import lombok.Data;

/**
 * @author Raph
 * AutoPass - backend.autopass.payload.viewmodels
 * StripeSessionStatusViewModel
 * The stripe session status view model for return page.4
 * 4/6/2024
 */
@Data
@Builder
public class StripeSessionStatusViewModel {
    String status;
    String customerEmail;
}
