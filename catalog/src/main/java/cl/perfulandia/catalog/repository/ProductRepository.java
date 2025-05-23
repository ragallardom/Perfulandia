package cl.perfulandia.catalog.repository;

import cl.perfulandia.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
