package cl.perfulandia.suppliers.service;



import cl.perfulandia.suppliers.model.*;
import cl.perfulandia.suppliers.repository.ReplenishmentOrderRepository;
import cl.perfulandia.suppliers.repository.SupplierRepository;
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
    public ReplenishmentOrder createOrder(Long supplierId, List<OrderItem> items) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        ReplenishmentOrder order = new ReplenishmentOrder();
        order.setCode(generateOrderCode());
        order.setSupplier(supplier);

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<ReplenishmentOrder> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId);
    }

    @Transactional(readOnly = true)
    public Optional<ReplenishmentOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public ReplenishmentOrder updateOrderStatus(Long id, ReplenishmentOrder.Status newStatus) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(newStatus);
                    if (newStatus == ReplenishmentOrder.Status.IN_PROCESS) {
                        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
                    }
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ReplenishmentOrder> getOrdersByStatus(ReplenishmentOrder.Status status) {
        return orderRepository.findByStatus(status);
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
