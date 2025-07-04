package cl.perfulandia.logistics.service;

import cl.perfulandia.logistics.model.Shipment;
import cl.perfulandia.logistics.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public Shipment createShipment(Shipment shipment) {
        shipment.setCreatedAt(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment.setCurrentStatus("Preparando");
        return repository.save(shipment);
    }

    public List<Shipment> getAllShipments() {
        return repository.findAll();
    }

    public Shipment updateStatus(Long id, String status) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        shipment.setCurrentStatus(status);
        shipment.setUpdatedAt(LocalDateTime.now());
        return repository.save(shipment);
    }


}
