package cl.perfulandia.billing.service;

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
        Invoice invoice = new Invoice();
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Invoice result = service.createInvoice(invoice);

        assertNotNull(result.getDateIssued());
        assertFalse(result.getPaid());
        verify(repository).save(invoice);
    }

    @Test
    void markAsPaidUpdatesInvoice() {
        Invoice invoice = new Invoice();
        invoice.setPaid(false);
        when(repository.findById(1L)).thenReturn(Optional.of(invoice));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Invoice result = service.markAsPaid(1L);

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
        when(repository.findById(2L)).thenReturn(Optional.of(invoice));

        Invoice result = service.getInvoiceById(2L);

        assertSame(invoice, result);
        verify(repository).findById(2L);
    }
}
