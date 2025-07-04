package cl.perfulandia.logistics.repository;

import cl.perfulandia.logistics.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

}

