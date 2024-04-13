package backend.autopass.service;

import backend.autopass.model.entities.TransitLog;
import backend.autopass.model.repositories.TransitLogRepository;
import backend.autopass.payload.dto.TransitLogPageDTO;
import backend.autopass.payload.viewmodels.TransitLogPageViewModel;
import backend.autopass.service.interfaces.ITransitLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Raphael Paquin
 * @version 01
 * The transit log service implementation.
 * 2024-04-12
 * AutoPass
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TransitLogService implements ITransitLogService {

    private final TransitLogRepository transitLogRepository;

    @Override
    public TransitLogPageViewModel getAllTransitLogs(TransitLogPageDTO dto) {
        try {
            Collection<TransitLog> transitLogs = transitLogRepository.getAllByUserId(dto.userId);

            // Filter based on year and month if specified
            if (dto.year != 0 || dto.month != 0) {
                transitLogs = transitLogs.stream().filter(log -> {
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis((long) log.getDate());
                    boolean isYearMatch = (dto.year == 0 || time.get(Calendar.YEAR) == dto.year);
                    boolean isMonthMatch = (dto.month == 0 || time.get(Calendar.MONTH) + 1 == dto.getMonth());
                    return isYearMatch && isMonthMatch;
                }).collect(Collectors.toList());
            }

            // Calculate the number of pages
            int PAGE_COUNT = 25;
            int pageCount = (int) Math.ceil((double) transitLogs.size() / PAGE_COUNT);

            // Apply pagination
            int start = (dto.page - 1) * PAGE_COUNT;
            int end = Math.min(start + PAGE_COUNT, transitLogs.size());
            transitLogs = new ArrayList<>(transitLogs).subList(start, end);

            // Build the page view model
            return TransitLogPageViewModel
                    .builder()
                    .page(dto.page)
                    .pageCount(pageCount)
                    .transitLogs(transitLogs)
                    .build();

        } catch (Exception e) {
            log.error("Error loading user transit logs", e);
            return null;
        }
    }

}
