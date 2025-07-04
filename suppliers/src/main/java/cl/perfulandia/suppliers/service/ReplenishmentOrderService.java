package cl.perfulandia.suppliers.service;



import cl.perfulandia.suppliers.model.*;
import cl.perfulandia.suppliers.repository.ReplenishmentOrderRepository;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import cl.perfulandia.suppliers.dto.ReplenishmentOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class ReplenishmentOrderService {

    private final ReplenishmentOrderRepository orderRepository;
    private final SupplierRepository supplierRepository;

    public ReplenishmentOrderService(ReplenishmentOrderRepository orderRepository,
                                     SupplierRepository supplierRepository) {
        this.orderRepository = orderRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ReplenishmentOrderResponse createOrder(Long supplierId, List<OrderItem> items) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        ReplenishmentOrder order = new ReplenishmentOrder();
        order.setCode(generateOrderCode());
        order.setSupplier(supplier);

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        return toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<ReplenishmentOrderResponse> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ReplenishmentOrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    @Transactional
    public ReplenishmentOrderResponse updateOrderStatus(Long id, ReplenishmentOrder.Status newStatus) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(newStatus);
                    if (newStatus == ReplenishmentOrder.Status.IN_PROCESS) {
                        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
                    }
                    return toResponse(orderRepository.save(order));
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ReplenishmentOrderResponse> getOrdersByStatus(ReplenishmentOrder.Status status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    private ReplenishmentOrderResponse toResponse(ReplenishmentOrder order) {
        ReplenishmentOrderResponse resp = new ReplenishmentOrderResponse();
        resp.setId(order.getId());
        resp.setCode(order.getCode());
        resp.setCreationDate(order.getCreationDate());
        resp.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());
        resp.setStatus(order.getStatus().name());
        resp.setSupplierId(order.getSupplier().getId());
        if (order.getItems() != null) {
            List<ReplenishmentOrderResponse.OrderItemDto> items = order.getItems().stream().map(i -> {
                ReplenishmentOrderResponse.OrderItemDto dto = new ReplenishmentOrderResponse.OrderItemDto();
                dto.setId(i.getId());
                dto.setProductCode(i.getProductCode());
                dto.setProductName(i.getProductName());
                dto.setQuantity(i.getQuantity());
                dto.setUnitPrice(i.getUnitPrice());
                return dto;
            }).toList();
            resp.setItems(items);
        }
        return resp;
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
