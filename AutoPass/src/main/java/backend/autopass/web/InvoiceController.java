package backend.autopass.web;

import backend.autopass.model.entities.Invoice;
import backend.autopass.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author Raphael Paquin
 * @version 01
 * The controller for managing invoices.
 * 2024-04-11
 * AutoPass
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/invoice")
@PreAuthorize("hasAnyRole('ADMIN','USER','GOOGLE_USER')")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/invoices")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(description = "Gets all the scanner accounts found in the database that are not deleted.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Invoice.class)))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "BAD_REQUEST",
                    content = @Content
            )
    })
    public ResponseEntity<Collection<Invoice>> getAllInvoicesByUserId(@RequestParam int userId) {

        try {
            return ResponseEntity.ok(invoiceService.getInvoicesByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
