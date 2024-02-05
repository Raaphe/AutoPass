package backend.autopass.payload.dto;

import backend.autopass.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "firstname is required")
    @Size(min = 5, max = 50)
    private String firstname;
    @NotBlank(message = "lastname is required")
    @Size(min = 5, max = 50)
    private String lastname;
    @NotBlank(message = "email is required")
    @Size(min = 5, max = 200)
    @Email(message = "email format is not valid")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 5, max = 255)
    private String password;
    @NotNull
    private Role role;
}
