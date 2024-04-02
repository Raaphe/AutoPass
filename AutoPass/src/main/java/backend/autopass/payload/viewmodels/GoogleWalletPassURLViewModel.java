package backend.autopass.payload.viewmodels;

import lombok.Builder;
import lombok.Data;

/**
 * @author Raph
 * AutoPass - backend.autopass.payload.viewmodels
 * GoogleWalletPassURLViewModel
 * View model for fetching Google wallet save pass URL.
 * 3/30/2024
 */
@Data
@Builder
public class GoogleWalletPassURLViewModel {
    String passUrl;
    Boolean isExpired;
}
