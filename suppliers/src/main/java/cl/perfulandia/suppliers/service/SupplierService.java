package cl.perfulandia.suppliers.service;

import cl.perfulandia.suppliers.model.Supplier;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProvider {

    private final SupplierRepository supplierRepository;

    public ServiceProvider(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier CreateSupplier(Supplier supplier){
        if(supplierRepository.existsByRut(supplier.getRut())){
            throw new IllegalArgumentException("Provider already exists");
        }
        if(supplierRepository.existsByEmail(supplier.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> ListSupplier(){
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> findByRut(String rut){
        return supplierRepository.findByRut(rut);
    }

    @Transactional( readOnly = true)
    public Optional<Supplier> GetSupplierByRut (long id){
        return supplierRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public Supplier updateSupplier (Long id, Supplier supplierUpdate){
        return SupplierRepository.findById(id).map(Supplier ->{
            if(!Supplier.getRut().equals(supplierUpdate.getRut()) && SupplierRepository.existsByRut(supplierUpdate.getRut())){
                throw new IllegalArgumentException("Provider already exists");
            }
            if (!Supplier.getEmail().equals(supplierUpdate.getEmail()) && SupplierRepository.existsByEmail(supplierUpdate.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            Supplier.setNombre(supplierUpdate.getNombre());
            Supplier.setDireccion(supplierUpdate.getDireccion());
            Supplier.setTelefono(supplierUpdate.getTelefono());
            Supplier.setEmail(supplierUpdate.getEmail());
            Supplier.setTipoProducto(supplierUpdate.getTipoProducto());
            return supplierRepository.save(Supplier);
        }).orElseThrow(() -> new IllegalArgumentException("Supplier don't found by id:"+ id));
    }

    @Transactional
    public void deleteSupplier (Long id){
        SupplierRepository.deleteById(id);
    }

}
