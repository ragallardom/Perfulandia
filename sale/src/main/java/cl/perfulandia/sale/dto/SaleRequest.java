package cl.perfulandia.sale.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SaleRequest {
    private Long userId;
    private Long productId;
    private Long branchId;
    private Integer quantity;

}
