package cl.perfulandia.catalog.service;

import cl.perfulandia.catalog.dto.ProductRequest;
import cl.perfulandia.catalog.model.Product;
import cl.perfulandia.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public Product create(ProductRequest req) {
        Product p = Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .category(req.getCategory())
                .build();
        return repo.save(p);
    }

    public Product update(Long id, ProductRequest req) {
        Product existing = getById(id);
        existing.setName(req.getName());
        existing.setDescription(req.getDescription());
        existing.setPrice(req.getPrice());
        existing.setCategory(req.getCategory());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
