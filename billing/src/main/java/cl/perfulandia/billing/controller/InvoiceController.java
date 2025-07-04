package cl.perfulandia.billing.controller;

import cl.perfulandia.billing.dto.InvoiceRequest;
import cl.perfulandia.billing.dto.InvoiceResponse;
import cl.perfulandia.billing.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping
    public InvoiceResponse create(@RequestBody InvoiceRequest invoice) {
        return service.createInvoice(invoice);
    }

    @GetMapping
    public List<InvoiceResponse> list() {
        return service.getAllInvoices();
    }

    @PutMapping("/{id}/pay")
    public InvoiceResponse pay(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInvoiceById(id));
    }
}
