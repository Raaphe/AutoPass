package backend.autopass.web;

import backend.autopass.model.entities.User;
import backend.autopass.payload.viewmodels.ProductsViewModel;
import backend.autopass.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@PreAuthorize("hasAnyRole('ADMIN','USER','GOOGLE_USER')")
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping(value = "/get-all-products")
    @Operation(description = "Gets all the products of the application that isn't deleted.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successfully returned products.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductsViewModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request",
                    content = @Content
            )
    })
    public ResponseEntity<ProductsViewModel> getAllProducts(){
        try {
            return ResponseEntity.ok(productsService.getAllProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
