package cl.perfulandia.suppliers.repository;

import cl.perfulandia.suppliers.model.ReplenishmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplenishmentOrderRepository extends JpaRepository<ReplenishmentOrder, Long> {
    List<ReplenishmentOrder> findByProveedorId(Long proveedorId);
    List<ReplenishmentOrder> findByEstado(ReplenishmentOrder.Estado estado);
    Optional<ReplenishmentOrder> findByCodigo(String codigo);
}
