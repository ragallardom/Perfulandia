package cl.perfulandia.suppliers.controller;
import cl.perfulandia.suppliers.model.OrderItem;
import cl.perfulandia.suppliers.model.ReplenishmentOrder;
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
    public ResponseEntity<ReplenishmentOrder> createOrder(
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

        ReplenishmentOrder order = orderService.createOrder(supplierId, items);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ReplenishmentOrder>> getOrdersBySupplier(
            @PathVariable Long supplierId) {
        List<ReplenishmentOrder> orders = orderService.getOrdersBySupplier(supplierId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReplenishmentOrder> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReplenishmentOrder> updateOrderStatus(
            @PathVariable Long id, @RequestParam String status) {
        try {
            ReplenishmentOrder.Status newStatus =
                    ReplenishmentOrder.Status.valueOf(status.toUpperCase());
            ReplenishmentOrder order = orderService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReplenishmentOrder>> getOrdersByStatus(
            @PathVariable String status) {
        try {
            ReplenishmentOrder.Status statusEnum =
                    ReplenishmentOrder.Status.valueOf(status.toUpperCase());
            List<ReplenishmentOrder> orders = orderService.getOrdersByStatus(statusEnum);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}






