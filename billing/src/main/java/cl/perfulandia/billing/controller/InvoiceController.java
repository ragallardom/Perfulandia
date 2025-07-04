package cl.perfulandia.billing.controller;

import cl.perfulandia.billing.model.Invoice;
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
    public Invoice create(@RequestBody Invoice invoice) {
        return service.createInvoice(invoice);
    }

    @GetMapping
    public List<Invoice> list() {
        return service.getAllInvoices();
    }

    @PutMapping("/{id}/pay")
    public Invoice pay(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInvoiceById(id));
    }
}
