package cl.perfulandia.suppliers.controller;
import cl.perfulandia.suppliers.dto.SupplierRequest;
import cl.perfulandia.suppliers.dto.SupplierResponse;
import cl.perfulandia.suppliers.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierRequest supplier) {
        SupplierResponse newSupplier = supplierService.createSupplier(supplier);
        return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        List<SupplierResponse> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-tax-id/{taxId}")
    public ResponseEntity<SupplierResponse> getSupplierByTaxId(@PathVariable String taxId) {
        return supplierService.getSupplierByTaxId(taxId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id, @RequestBody SupplierRequest updatedSupplier) {
        try {
            SupplierResponse supplier = supplierService.updateSupplier(id, updatedSupplier);
            return ResponseEntity.ok(supplier);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
