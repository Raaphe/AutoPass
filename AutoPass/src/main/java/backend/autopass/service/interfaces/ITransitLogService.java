package backend.autopass.service.interfaces;

import backend.autopass.payload.dto.TransitLogPageDTO;
import backend.autopass.payload.viewmodels.TransitLogPageViewModel;

/**
 * @author Raphael Paquin
 * @version 01
 * The transit log services method interface.
 * 2024-04-12
 * AutoPass
 */
public interface ITransitLogService {

    /**
     * Gets all of a user's transit logs.
     * @param dto The target user's id along with page filters.
     * @return `TransitLogPageViewModel` The list of transit logs with page info.
     */
    TransitLogPageViewModel getAllTransitLogs(TransitLogPageDTO dto);
}
