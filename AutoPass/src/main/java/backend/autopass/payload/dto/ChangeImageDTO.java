package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * ChangeImageDTO - 2024-03-30
 * Raph
 * DTO for Change image request.
 * AutoPass
 */
@Data
@Builder
public class ChangeImageDTO {
    MultipartFile image;
    int userId;
}
