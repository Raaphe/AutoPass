package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * ScannerRegistrationDTO - 2024-03-30
 * Raph
 * DTO for registering scanner accounts.
 * AutoPass
 */
@Data
@Builder
public class ScannerRegistrationDTO {

    @NonNull
    String pwd;
    @NonNull
    Integer busNumber;
    @NonNull
    String routeName;

}
