package cl.perfulandia.suppliers.service;

import cl.perfulandia.suppliers.model.Supplier;
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
        Supplier s = new Supplier();
        when(repo.save(any())).thenReturn(s);
        Supplier result = service.createSupplier(new Supplier());
        assertNotNull(result);
        verify(repo).save(any(Supplier.class));
    }

    @Test
    void createSupplierFailsWhenEmailExists() {
        Supplier s = new Supplier();
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

        Supplier update = new Supplier();
        update.setTaxId("2");
        update.setEmail("orig@test.com");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByTaxId("2")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.updateSupplier(1L, update));
    }
}
