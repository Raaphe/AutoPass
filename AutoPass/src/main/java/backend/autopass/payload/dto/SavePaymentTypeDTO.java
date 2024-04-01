package backend.autopass.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * SavePaymentTypeDTO - 2024-03-30
 * Raph
 * dto for request that saves payment types.
 * AutoPass
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavePaymentTypeDTO {

    @NotBlank(message = "The id for the payment method cannot be blank.")
    int paymentTypeId;
    @NotBlank(message = "Credit card needs a user.")
    int userId;
    @NotBlank
    Date expiryDate;
    @NotBlank(message = "Credit card CVV cannot be empty")
    @Size(min = 3, max = 3)
    String cVV;
    @NotBlank(message = "Credit card PAN cannot be null.")
    @Size(max = 10, min = 10)
    String pAN;

}
