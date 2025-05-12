package cl.perfulandia.branches.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
}
