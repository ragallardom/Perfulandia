package cl.perfulandia.suppliers.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReplenishmentOrderRequest {
    private Long supplierId;
    private List<OrderItemRequest> items;
}
