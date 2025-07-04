package cl.perfulandia.logistics.controller;

import cl.perfulandia.logistics.model.Shipment;
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
    public Shipment create(@RequestBody Shipment shipment) {
        return service.createShipment(shipment);
    }

    @GetMapping
    public List<Shipment> list() {
        return service.getAllShipments();
    }

    @PutMapping("/{id}/status")
    public Shipment updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }
}
