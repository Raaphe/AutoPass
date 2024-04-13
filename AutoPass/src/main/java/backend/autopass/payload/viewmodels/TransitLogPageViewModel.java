package backend.autopass.payload.viewmodels;

/**
 * @author Raphael Paquin
 * @version 01
 * The view model for a transit log page.
 * 2024-04-13
 * AutoPass
 */

import backend.autopass.model.entities.TransitLog;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class TransitLogPageViewModel {
    public Collection<TransitLog> transitLogs;
    public int page;
    public int pageCount;
}
