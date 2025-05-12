package cl.perfulandia.branches.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private String branchName;
    private String productName;
    private Integer stock;
}