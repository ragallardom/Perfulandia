package cl.perfulandia.logistics.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentServiceTest {
    @Test
    void serviceInstantiates() {
        ShipmentService service = new ShipmentService();
        assertNotNull(service);
    }
}
