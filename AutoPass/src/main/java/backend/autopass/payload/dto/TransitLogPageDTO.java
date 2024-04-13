package backend.autopass.payload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Raphael Paquin
 * @version 01
 * The dto for a transit history page.
 * 2024-04-13
 * AutoPass
 */
@Builder
@Data
public class TransitLogPageDTO {

    @Schema
    public int userId;
    @Schema
    public int year;
    @Schema
    public int month;
    @Schema
    public int page;
}
