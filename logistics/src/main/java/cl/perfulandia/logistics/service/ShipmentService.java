package cl.perfulandia.logistics.service;

import cl.perfulandia.logistics.model.Shipment;
import cl.perfulandia.logistics.repository.ShipmentRepository;
import cl.perfulandia.logistics.dto.ShipmentRequest;
import cl.perfulandia.logistics.dto.ShipmentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public ShipmentResponse createShipment(ShipmentRequest request) {
        Shipment shipment = new Shipment();
        shipment.setOrderCode(request.getOrderCode());
        shipment.setOrigin(request.getOrigin());
        shipment.setDestination(request.getDestination());
        shipment.setCreatedAt(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment.setCurrentStatus("Preparando");
        return toResponse(repository.save(shipment));
    }

    public List<ShipmentResponse> getAllShipments() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ShipmentResponse updateStatus(Long id, String status) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        shipment.setCurrentStatus(status);
        shipment.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(shipment));
    }

    private ShipmentResponse toResponse(Shipment shipment) {
        ShipmentResponse resp = new ShipmentResponse();
        resp.setId(shipment.getId());
        resp.setOrderCode(shipment.getOrderCode());
        resp.setCurrentStatus(shipment.getCurrentStatus());
        resp.setOrigin(shipment.getOrigin());
        resp.setDestination(shipment.getDestination());
        resp.setCreatedAt(shipment.getCreatedAt());
        resp.setUpdatedAt(shipment.getUpdatedAt());
        return resp;
    }


}
