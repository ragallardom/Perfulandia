package cl.perfulandia.suppliers.service;


import cl.perfulandia.suppliers.model.*;
import cl.perfulandia.suppliers.repository.ReplenishmentOrderRepository;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderReplenishmentService {

    private final ReplenishmentOrderRepository replenishmentOrderRepository;
    private final SupplierRepository supplierRepository;

    public OrderReplenishmentService (ReplenishmentOrderRepository replenishmentOrderRepository, SupplierRepository supplierRepository) {
        this.replenishmentOrderRepository = replenishmentOrderRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ReplenishmentOrder createOrder(long supplierId, List<ReplenishmentOrder> orders){
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(()->new RuntimeException("Supplier not found"));

        ReplenishmentOrder order = new ReplenishmentOrder();
        order.setId(generarCodigoOrden);
    }




}
