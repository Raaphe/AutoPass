package backend.autopass.web;
import backend.autopass.model.entities.User;
import backend.autopass.payload.dto.ChangeImageDTO;
import backend.autopass.payload.dto.UpdateUserDTO;
import backend.autopass.service.UserService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER', 'OAUTH2_USER', 'GOOGLE_USER')")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
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

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // or throw an exception based on your requirements
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ResponseEntity.ok(userService.getUserByEmail(((UserDetails) principal).getUsername()));
        }

        return null; // Handle accordingly
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

    @GetMapping("/get-user-pfp")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Gets a user's profile image url.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User image was fetched.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request.",
                    content = @Content
            ),
    })
    public ResponseEntity<String> getUserImage(int userId) {
        try {
            return ResponseEntity.ok().body(userService.getImageFromUser(userId));
        } catch (AwsServiceException | SdkClientException e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    @PostMapping("/upload-profile-image")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Updates a user's profile image.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User profile image is saved.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request.",
                    content = @Content
            ),
    })
    public ResponseEntity<String> setUserImage(@RequestParam("file") MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails currentUser = (UserDetails) authentication.getPrincipal();

            User user = userService.getUserByEmail(currentUser.getUsername());

            ChangeImageDTO dto = ChangeImageDTO
                    .builder()
                    .image(file)
                    .userId(user.getId())
                    .build();
            return ResponseEntity.ok().body(this.userService.saveImageToUser(dto));
        } catch (AwsServiceException | SdkClientException e) {
            return ResponseEntity.badRequest().body("");
        }
    }
}