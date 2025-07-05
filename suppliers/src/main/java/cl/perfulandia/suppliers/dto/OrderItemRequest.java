package cl.perfulandia.suppliers.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String productCode;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}
