package backend.autopass.web;

import backend.autopass.payload.viewmodels.PassValidationResponseViewModel;
import backend.autopass.service.ScannerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raph
 * AutoPass - backend.autopass.web
 * TerminalController
 * The REST Controller responsible for terminal operations.
 * 3/31/2024
 */
@RestController
@PreAuthorize("hasAnyRole('SCANNER_USER')")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/terminal")
public class TerminalController {

    private final ScannerService scannerService;

    @GetMapping("/validate")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Validates a google wallet QR-Code.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Authorization Successful.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PassValidationResponseViewModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad Request.",
                    content = @Content
            )
    })
    public ResponseEntity<PassValidationResponseViewModel> validatePass(@RequestParam String barcodeValue) {
        try {
            return ResponseEntity.ok().body(scannerService.validatePass(barcodeValue));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
