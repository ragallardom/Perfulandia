package cl.perfulandia.suppliers.repository;

import cl.perfulandia.suppliers.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository <Supplier, Long>{
    Optional<Supplier> findByTaxId(String taxId);
    Optional<Supplier> findByEmail(String email);
    boolean existsByTaxId(String taxId);
    boolean existsByEmail(String email);
}
