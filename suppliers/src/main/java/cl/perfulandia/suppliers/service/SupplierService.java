package cl.perfulandia.suppliers.service;

import cl.perfulandia.suppliers.model.Supplier;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import cl.perfulandia.suppliers.dto.SupplierRequest;
import cl.perfulandia.suppliers.dto.SupplierResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier(
                request.getTaxId(),
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                request.getProductType()
        );
        if (supplierRepository.existsByTaxId(supplier.getTaxId())) {
            throw new IllegalArgumentException("Supplier with this tax ID already exists");
        }
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new IllegalArgumentException("Supplier with this email already exists");
        }
        return toResponse(supplierRepository.save(supplier));
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SupplierResponse> getSupplierById(Long id) {
        return supplierRepository.findById(id).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<SupplierResponse> getSupplierByTaxId(String taxId) {
        return supplierRepository.findByTaxId(taxId).map(this::toResponse);
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest updatedSupplier) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    if (!supplier.getTaxId().equals(updatedSupplier.getTaxId()) &&
                            supplierRepository.existsByTaxId(updatedSupplier.getTaxId())) {
                        throw new IllegalArgumentException("Supplier with this tax ID already exists");
                    }
                    if (!supplier.getEmail().equals(updatedSupplier.getEmail()) &&
                            supplierRepository.existsByEmail(updatedSupplier.getEmail())) {
                        throw new IllegalArgumentException("Supplier with this email already exists");
                    }
                    supplier.setName(updatedSupplier.getName());
                    supplier.setAddress(updatedSupplier.getAddress());
                    supplier.setPhone(updatedSupplier.getPhone());
                    supplier.setEmail(updatedSupplier.getEmail());
                    supplier.setProductType(updatedSupplier.getProductType());
                    return toResponse(supplierRepository.save(supplier));
                })
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + id));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    private SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse resp = new SupplierResponse();
        resp.setId(supplier.getId());
        resp.setTaxId(supplier.getTaxId());
        resp.setName(supplier.getName());
        resp.setAddress(supplier.getAddress());
        resp.setPhone(supplier.getPhone());
        resp.setEmail(supplier.getEmail());
        resp.setProductType(supplier.getProductType());
        return resp;
    }
}
