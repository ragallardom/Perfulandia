package cl.perfulandia.user.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String role;
}