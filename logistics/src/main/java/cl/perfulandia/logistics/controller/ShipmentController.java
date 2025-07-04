package cl.perfulandia.logistics.controller;

import cl.perfulandia.logistics.dto.ShipmentRequest;
import cl.perfulandia.logistics.dto.ShipmentResponse;
import cl.perfulandia.logistics.service.ShipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @PostMapping
    public ShipmentResponse create(@RequestBody ShipmentRequest shipment) {
        return service.createShipment(shipment);
    }

    @GetMapping
    public List<ShipmentResponse> list() {
        return service.getAllShipments();
    }

    @PutMapping("/{id}/status")
    public ShipmentResponse updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }
}
