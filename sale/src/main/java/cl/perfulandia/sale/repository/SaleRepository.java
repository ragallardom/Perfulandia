package cl.perfulandia.sale.repository;

import cl.perfulandia.sale.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByBranchId(Long branchId);
}