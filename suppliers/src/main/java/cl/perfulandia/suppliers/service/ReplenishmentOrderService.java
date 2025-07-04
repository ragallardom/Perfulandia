package cl.perfulandia.suppliers.service;


import cl.perfulandia.suppliers.model.OrderItem;
import cl.perfulandia.suppliers.model.ReplenishmentOrder;
import cl.perfulandia.suppliers.model.Supplier;
import cl.perfulandia.suppliers.repository.ReplenishmentOrderRepository;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReplenishmentOrderService {

    private final ReplenishmentOrderRepository replenishmentOrderRepository;
    private final SupplierRepository supplierRepository;

    public ReplenishmentOrderService(ReplenishmentOrderRepository replenishmentOrderRepository, SupplierRepository supplierRepository) {
        this.replenishmentOrderRepository = replenishmentOrderRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ReplenishmentOrder createOrder(long supplierId, List<OrderItem> items){
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        ReplenishmentOrder order = new ReplenishmentOrder();
        order.setCodigo(UUID.randomUUID().toString());
        order.setProveedor(supplier);
        order.setEstado(ReplenishmentOrder.Estado.PENDIENTE);
        order.setItems(items);
        if (items != null) {
            items.forEach(i -> i.setOrden(order));
        }
        return replenishmentOrderRepository.save(order);
    }




}
