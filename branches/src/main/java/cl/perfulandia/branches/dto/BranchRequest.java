package cl.perfulandia.branches.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BranchRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Size(max = 200)
    private String address;
}