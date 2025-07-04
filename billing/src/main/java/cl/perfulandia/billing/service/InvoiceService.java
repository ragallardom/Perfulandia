package cl.perfulandia.billing.service;

import cl.perfulandia.billing.model.Invoice;
import cl.perfulandia.billing.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public Invoice createInvoice(Invoice invoice) {
        invoice.setDateIssued(LocalDateTime.now());
        invoice.setPaid(false);
        return repository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return repository.findAll();
    }

    public Invoice markAsPaid(Long id) {
        Invoice invoice = repository.findById(id).orElseThrow();
        invoice.setPaid(true);
        return repository.save(invoice);
    }

    public Invoice getInvoiceById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}