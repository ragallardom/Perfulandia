package cl.perfulandia.suppliers.controller;
import cl.perfulandia.suppliers.model.OrderItem;
import cl.perfulandia.suppliers.model.ReplenishmentOrder;
import cl.perfulandia.suppliers.dto.OrderItemRequest;
import cl.perfulandia.suppliers.dto.ReplenishmentOrderResponse;
import cl.perfulandia.suppliers.service.ReplenishmentOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replenishment-orders")
public class ReplenishmentOrderController {
    private final ReplenishmentOrderService orderService;

    public ReplenishmentOrderController(ReplenishmentOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ReplenishmentOrderResponse> createOrder(
            @RequestParam Long supplierId,
            @RequestBody List<OrderItemRequest> itemsRequest) {

        List<OrderItem> items = itemsRequest.stream()
                .map(item -> new OrderItem(
                        item.getProductCode(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        ReplenishmentOrderResponse order = orderService.createOrder(supplierId, items);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ReplenishmentOrderResponse>> getOrdersBySupplier(
            @PathVariable Long supplierId) {
        List<ReplenishmentOrderResponse> orders = orderService.getOrdersBySupplier(supplierId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReplenishmentOrderResponse> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReplenishmentOrderResponse> updateOrderStatus(
            @PathVariable Long id, @RequestParam String status) {
        try {
            ReplenishmentOrder.Status newStatus =
                    ReplenishmentOrder.Status.valueOf(status.toUpperCase());
            ReplenishmentOrderResponse order = orderService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReplenishmentOrderResponse>> getOrdersByStatus(
            @PathVariable String status) {
        try {
            ReplenishmentOrder.Status statusEnum =
                    ReplenishmentOrder.Status.valueOf(status.toUpperCase());
            List<ReplenishmentOrderResponse> orders = orderService.getOrdersByStatus(statusEnum);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}






