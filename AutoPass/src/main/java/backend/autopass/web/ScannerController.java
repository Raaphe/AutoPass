package backend.autopass.web;

import backend.autopass.model.entities.User;
import backend.autopass.service.ScannerService;
import backend.autopass.service.interfaces.ScannerRegistrationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/admin/scanner")
@RequiredArgsConstructor
public class ScannerController {

    private final ScannerService scannerService;

    @GetMapping("/scanners")
    @Operation(description = "Gets all the scanner accounts found in the database that are not deleted.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Scanners sent successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Scanners not found",
                    content = @Content
            )
    })
    public ResponseEntity<Collection<User>> getAllScanners() {
        try {
            Collection<User> scanners = scannerService.getAllScanner();
            return ResponseEntity.ok().body(scanners);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create-scanner")
    @Operation(description = "Takes a scanner registration object to create a scanner account.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Scanners created successfully. Returned entity is the account with newly generated email.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Registration impossible.",
                    content = @Content
            )
    })
    public ResponseEntity<User> createScannerAccount(@RequestBody ScannerRegistrationDTO dto) {
        try {
            return ResponseEntity.ok().body(scannerService.registerScanner(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/delete-bus")
    @Operation(description = "Deleted a bus.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Scanner successfully deleted.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Deletion error.",
                    content = @Content
            )
    })
    public ResponseEntity<Boolean> DeleteScanner(@RequestParam Integer busNumber) {
        try {
            return ResponseEntity.ok().body(scannerService.deletedBus(busNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/scanner-info")
    @Operation(description = "Gets a scanner account from bus number.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Scanner successfully deleted.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Could not fetch scanner.",
                    content = @Content
            )
    })
    public ResponseEntity<User> getScannerByBusNumber(@RequestParam Integer busNumber) {
        try {
            return ResponseEntity.ok().body(scannerService.getBusFromBusNumber(busNumber));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
