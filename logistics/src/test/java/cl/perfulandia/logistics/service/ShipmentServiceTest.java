package cl.perfulandia.logistics.service;

import cl.perfulandia.logistics.dto.ShipmentRequest;
import cl.perfulandia.logistics.dto.ShipmentResponse;
import cl.perfulandia.logistics.model.Shipment;
import cl.perfulandia.logistics.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    @Mock
    private ShipmentRepository repository;
    @InjectMocks
    private ShipmentService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShipmentInitializesAndSaves() {
        ShipmentRequest req = new ShipmentRequest();
        req.setOrderCode("O1");
        req.setOrigin("Orig");
        req.setDestination("Dest");

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        ShipmentResponse result = service.createShipment(req);

        assertNotNull(result.getCreatedAt());
        assertEquals("Preparando", result.getCurrentStatus());
        verify(repository).save(any(Shipment.class));
    }

    @Test
    void updateStatusUpdatesAndPersists() {
        Shipment shipment = new Shipment();
        shipment.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(shipment));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        ShipmentResponse result = service.updateStatus(1L, "Enviado");

        assertEquals("Enviado", result.getCurrentStatus());
        verify(repository).findById(1L);
        verify(repository).save(shipment);
    }
}
