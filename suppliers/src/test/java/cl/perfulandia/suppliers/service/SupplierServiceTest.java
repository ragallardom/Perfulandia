package cl.perfulandia.suppliers.service;

import cl.perfulandia.suppliers.model.Supplier;
import cl.perfulandia.suppliers.dto.SupplierRequest;
import cl.perfulandia.suppliers.dto.SupplierResponse;
import cl.perfulandia.suppliers.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceTest {
    @Mock
    private SupplierRepository repo;
    @InjectMocks
    private SupplierService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSupplierSaves() {
        Supplier saved = new Supplier();
        when(repo.save(any())).thenReturn(saved);

        SupplierRequest request = new SupplierRequest();
        SupplierResponse result = service.createSupplier(request);

        assertNotNull(result);
        verify(repo).save(any(Supplier.class));
    }

    @Test
    void createSupplierFailsWhenEmailExists() {
        SupplierRequest s = new SupplierRequest();
        s.setEmail("existing@test.com");

        when(repo.existsByTaxId(any())).thenReturn(false);
        when(repo.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createSupplier(s));
    }

    @Test
    void updateSupplierThrowsWhenTaxIdExists() {
        Supplier existing = new Supplier();
        existing.setId(1L);
        existing.setTaxId("1");
        existing.setEmail("orig@test.com");

        SupplierRequest update = new SupplierRequest();
        update.setTaxId("2");
        update.setEmail("orig@test.com");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByTaxId("2")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.updateSupplier(1L, update));
    }
}
