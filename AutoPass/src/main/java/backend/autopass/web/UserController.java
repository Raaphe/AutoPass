package backend.autopass.web;
import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.UpdateUserDTO;
import backend.autopass.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9090"})
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/info")
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
    public ResponseEntity<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/delete-user")
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
                    responseCode = "409", description = "User already marked as deleted",
                    content = @Content
            )
    })
    public ResponseEntity<?> markUserAsDeleted() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            userService.deleteUser((long) currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception response) {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }


    }

    @PutMapping("/update-user-info")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Updates a user info by their ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User info updated.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404", description = "User not found.",
                    content = @Content
            )
    })
    public ResponseEntity<?> updateUser(UpdateUserDTO userNewInfo) {
        if (userService.updateUser(userNewInfo)) {
            return (ResponseEntity<?>) ResponseEntity.ok();
        } else {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
    }
}