package cl.perfulandia.suppliers.repository;

import cl.perfulandia.suppliers.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository <Supplier, Long>{
    Optional<Supplier> findByRut(String rut);
    Optional<Supplier> findByEmail(String email);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
}
