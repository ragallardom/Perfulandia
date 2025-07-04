package cl.perfulandia.suppliers.repository;

import cl.perfulandia.suppliers.model.ReplenishmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReplenishmentOrderRepository extends JpaRepository<ReplenishmentOrder, Long>{
    List<ReplenishmentOrder> findBySupplierId(Long supplierId);
    List<ReplenishmentOrder> findByStatus(ReplenishmentOrder.Status status);
    Optional<ReplenishmentOrder> findByCode(String code);
}
