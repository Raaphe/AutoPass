package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

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
