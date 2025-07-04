package cl.perfulandia.suppliers.service;

import cl.perfulandia.suppliers.model.Supplier;
import cl.perfulandia.suppliers.repository.SupplierRepository;
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
    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.existsByTaxId(supplier.getTaxId())) {
            throw new IllegalArgumentException("Supplier with this tax ID already exists");
        }
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new IllegalArgumentException("Supplier with this email already exists");
        }
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByTaxId(String taxId) {
        return supplierRepository.findByTaxId(taxId);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
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
                    return supplierRepository.save(supplier);
                })
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + id));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
