package cl.perfulandia.billing.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceServiceTest {
    @Test
    void serviceInstantiates() {
        InvoiceService service = new InvoiceService();
        assertNotNull(service);
    }
}
