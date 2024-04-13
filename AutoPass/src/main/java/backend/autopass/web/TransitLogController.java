package backend.autopass.web;

import backend.autopass.payload.dto.TransitLogPageDTO;
import backend.autopass.payload.viewmodels.TransitLogPageViewModel;
import backend.autopass.service.TransitLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Raphael Paquin
 * @version 01
 * The transit log REST controller.
 * 2024-04-12
 * AutoPass
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/transit-log")
@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GOOGLE_USER')")
@Slf4j
public class TransitLogController {

    private final TransitLogService transitLogService;

    @PostMapping("transit-logs")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Gets all of the transit logs of a given user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransitLogPageViewModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "BAD_REQUEST",
                    content = @Content
            )
    })
    public ResponseEntity<TransitLogPageViewModel> getUserTransitHistory(@RequestBody TransitLogPageDTO dto) {
        try {
            return ResponseEntity.ok(transitLogService.getAllTransitLogs(dto));
        } catch (Exception e) {
            log.error("Could not get user transit logs across HTTP.");
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
