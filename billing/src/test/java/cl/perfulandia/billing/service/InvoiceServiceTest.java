package cl.perfulandia.billing.service;

import cl.perfulandia.billing.dto.InvoiceRequest;
import cl.perfulandia.billing.dto.InvoiceResponse;
import cl.perfulandia.billing.model.Invoice;
import cl.perfulandia.billing.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository repository;
    @InjectMocks
    private InvoiceService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInvoiceSavesAndInitializes() {
        InvoiceRequest req = new InvoiceRequest();
        req.setCustomerName("John");
        req.setAmount(10.0);
        req.setPaymentMethod("card");

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InvoiceResponse result = service.createInvoice(req);

        assertNotNull(result.getDateIssued());
        assertFalse(result.getPaid());
        verify(repository).save(any(Invoice.class));
    }

    @Test
    void markAsPaidUpdatesInvoice() {
        Invoice invoice = new Invoice();
        invoice.setPaid(false);
        when(repository.findById(1L)).thenReturn(Optional.of(invoice));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InvoiceResponse result = service.markAsPaid(1L);

        assertTrue(result.getPaid());
        verify(repository).findById(1L);
        verify(repository).save(invoice);
    }

    @Test
    void getAllInvoicesDelegatesToRepository() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(0, service.getAllInvoices().size());
        verify(repository).findAll();
    }

    @Test
    void getInvoiceByIdReturnsValue() {
        Invoice invoice = new Invoice();
        invoice.setId(2L);
        invoice.setCustomerName("Jane");
        invoice.setAmount(20.0);
        invoice.setPaymentMethod("cash");
        when(repository.findById(2L)).thenReturn(Optional.of(invoice));

        InvoiceResponse result = service.getInvoiceById(2L);

        assertEquals(2L, result.getId());
        verify(repository).findById(2L);
    }
}
