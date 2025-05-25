package cl.perfulandia.sale.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class SaleResponse {
    private Long saleId;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime timestamp;

    // getters and setters
}