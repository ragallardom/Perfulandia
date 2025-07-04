package cl.perfulandia.billing.service;

import cl.perfulandia.billing.model.Invoice;
import cl.perfulandia.billing.repository.InvoiceRepository;
import cl.perfulandia.billing.dto.InvoiceRequest;
import cl.perfulandia.billing.dto.InvoiceResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public InvoiceResponse createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setCustomerName(request.getCustomerName());
        invoice.setAmount(request.getAmount());
        invoice.setPaymentMethod(request.getPaymentMethod());
        invoice.setDateIssued(LocalDateTime.now());
        invoice.setPaid(false);
        return toResponse(repository.save(invoice));
    }

    public List<InvoiceResponse> getAllInvoices() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public InvoiceResponse markAsPaid(Long id) {
        Invoice invoice = repository.findById(id).orElseThrow();
        invoice.setPaid(true);
        return toResponse(repository.save(invoice));
    }

    public InvoiceResponse getInvoiceById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow();
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse resp = new InvoiceResponse();
        resp.setId(invoice.getId());
        resp.setCustomerName(invoice.getCustomerName());
        resp.setAmount(invoice.getAmount());
        resp.setDateIssued(invoice.getDateIssued());
        resp.setPaid(invoice.getPaid());
        resp.setPaymentMethod(invoice.getPaymentMethod());
        return resp;
    }
}