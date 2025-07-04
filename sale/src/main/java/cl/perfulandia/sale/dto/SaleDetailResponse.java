package cl.perfulandia.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaleDetailResponse {
    private Long saleId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime timestamp;
}
