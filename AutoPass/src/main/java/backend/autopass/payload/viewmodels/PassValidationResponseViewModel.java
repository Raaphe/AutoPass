package backend.autopass.payload.viewmodels;

import lombok.Builder;
import lombok.Data;

/**
 * @author Raph
 * AutoPass - backend.autopass.payload.viewmodels
 * PassValidationResponseViewModel
 * The Response object after a pass is scanned.
 * 3/31/2024
 */
@Data
@Builder
public class PassValidationResponseViewModel {
    int numberOfTickets;
    double expiresAt;
    Boolean isValid;
    String responseMessage;
}
