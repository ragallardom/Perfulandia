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
        if(supplierRepository.existsByRut(supplier.getRut())){
            throw new IllegalArgumentException("Provider already exists");
        }
        if(supplierRepository.existsByEmail(supplier.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> listSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> findByRut(String rut){
        return supplierRepository.findByRut(rut);
    }

    @Transactional( readOnly = true)
    public Optional<Supplier> getSupplierById(long id) {
        return supplierRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public Supplier updateSupplier(Long id, Supplier supplierUpdate) {
        return supplierRepository.findById(id).map(existing -> {
            if (!existing.getRut().equals(supplierUpdate.getRut()) && supplierRepository.existsByRut(supplierUpdate.getRut())) {
                throw new IllegalArgumentException("Provider already exists");
            }
            if (!existing.getEmail().equals(supplierUpdate.getEmail()) && supplierRepository.existsByEmail(supplierUpdate.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            existing.setNombre(supplierUpdate.getNombre());
            existing.setDireccion(supplierUpdate.getDireccion());
            existing.setTelefono(supplierUpdate.getTelefono());
            existing.setEmail(supplierUpdate.getEmail());
            existing.setTipoProducto(supplierUpdate.getTipoProducto());
            return supplierRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Supplier don't found by id:" + id));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

}
