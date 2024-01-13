package backend.autopass.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class UserDTO {

    @Email
    @NotBlank
    public String email;
    @NotBlank
    @Size(min = 5, max = 255)
    public String pwd;
    @NotBlank
    @Size(max = 15)
    public String firstName;
    @NotBlank
    @Size(max = 15)
    public String lastName;

    public UserDTO(String email, String pwd, String firstName, String lastName) {
        this.email = email;
        this.pwd = pwd;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
