package backend.autopass.payload.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ChangeImageDTO {
    MultipartFile image;
    int userId;
}
