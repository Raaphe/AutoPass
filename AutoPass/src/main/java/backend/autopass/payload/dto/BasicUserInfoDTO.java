package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Raph
 * AutoPass - backend.autopass.payload.dto
 * BasicUserInfoDTO
 * <p>
 * <p>
 * 4/6/2024
 */
@Data
@Builder
public class BasicUserInfoDTO {
    int id;
    private String email;
    private String firstName;
    private String lastName;
}
