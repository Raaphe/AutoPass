package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Raphael Paquin
 * @version 01
 * The DTO when validating a Google Wallet Pass.
 * 2024-04-12
 * AutoPass
 */
@Data
@Builder
public class GoogleWalletPassValidationDTO {
    public String rotatingBarcodeValue;
    public int busNumber;
}
