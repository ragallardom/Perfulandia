package cl.perfulandia.user.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserRequest {
    @NotBlank @NotNull
    private String username;
    @NotBlank private String password;
    @NotBlank private String role;
}