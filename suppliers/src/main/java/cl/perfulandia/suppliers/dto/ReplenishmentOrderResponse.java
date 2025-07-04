package cl.perfulandia.suppliers.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReplenishmentOrderResponse {
    private Long id;
    private String code;
    private LocalDateTime creationDate;
    private LocalDateTime estimatedDeliveryDate;
    private String status;
    private Long supplierId;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long id;
        private String productCode;
        private String productName;
        private Integer quantity;
        private Double unitPrice;
    }
}
