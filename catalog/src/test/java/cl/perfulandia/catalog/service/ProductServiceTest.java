package cl.perfulandia.catalog.service;

import cl.perfulandia.catalog.dto.ProductRequest;
import cl.perfulandia.catalog.model.Product;
import cl.perfulandia.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repo;
    @InjectMocks
    private ProductService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsList() {
        Product p = new Product();
        when(repo.findAll()).thenReturn(List.of(p));

        List<Product> result = service.getAll();
        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void getByIdFound() {
        Product p = new Product();
        p.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product result = service.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getByIdNotFoundThrows() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(2L));
    }

    @Test
    void createSavesProduct() {
        ProductRequest req = new ProductRequest();
        req.setName("n");
        req.setDescription("d");
        req.setPrice(BigDecimal.valueOf(1.0));
        req.setCategory("c");
        Product saved = new Product();
        saved.setId(3L);
        when(repo.save(any())).thenReturn(saved);

        Product result = service.create(req);
        assertEquals(3L, result.getId());
        verify(repo).save(any(Product.class));
    }

    @Test
    void updateModifiesExisting() {
        Product existing = new Product();
        existing.setId(4L);
        existing.setName("old");
        when(repo.findById(4L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        ProductRequest req = new ProductRequest();
        req.setName("new");
        req.setDescription("d");
        req.setPrice(BigDecimal.valueOf(2.0));
        req.setCategory("c");

        Product result = service.update(4L, req);
        assertEquals("new", result.getName());
    }

    @Test
    void deleteDelegates() {
        service.delete(5L);
        verify(repo).deleteById(5L);
    }
}
