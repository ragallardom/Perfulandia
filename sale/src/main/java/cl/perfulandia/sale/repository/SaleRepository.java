package cl.perfulandia.sale.repository;

import cl.perfulandia.sale.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}