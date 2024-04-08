package backend.autopass.payload.dto;

import lombok.Data;

/**
 * @author Raph
 * AutoPass - backend.autopass.payload.dto
 * GetClientStripeSecretDTO
 * <p>
 * <p>
 * 4/6/2024
 */
@Data
public class GetClientStripeSecretDTO {
    String email;
    String priceId;
}

