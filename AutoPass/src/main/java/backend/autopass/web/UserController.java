package backend.autopass.web;

import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.SignUpDTO;
import backend.autopass.payload.viewmodels.AuthenticationResponse;
import backend.autopass.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.jta.UserTransactionAdapter;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9090"})
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequiredArgsConstructor
@RequestMapping("/user")

public class UserController {

    private final UserService userService;
    @GetMapping("/user")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Gets a user's information by their ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<User> getUser(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/user{id}/markAsDeleted")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Soft delete a user's by their ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User marked deleted.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404", description = "User already marked as deleted",
                    content = @Content
            )
    })
    public ResponseEntity<?> markUserAsDeleted(@PathVariable Long id) throws Exception {
            userService.markUserAsDeleted(id);
            return ResponseEntity.ok().build();

    }

}
