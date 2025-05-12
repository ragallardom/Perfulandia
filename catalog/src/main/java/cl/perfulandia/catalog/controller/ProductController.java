package cl.perfulandia.catalog.controller;

import cl.perfulandia.catalog.dto.ProductRequest;
import cl.perfulandia.catalog.dto.ProductResponse;
import cl.perfulandia.catalog.model.Product;
import cl.perfulandia.catalog.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponse> list() {
        return service.getAll().stream()
                .map(p -> ProductResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .category(p.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponse getOne(@PathVariable Long id) {
        Product p = service.getById(id);
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .category(p.getCategory())
                .build();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Validated ProductRequest req) {
        Product p = service.create(req);
        ProductResponse resp = ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .category(p.getCategory())
                .build();
        return ResponseEntity
                .created(URI.create("/products/" + resp.getId()))
                .body(resp);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id,
                                  @RequestBody @Validated ProductRequest req) {
        Product p = service.update(id, req);
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .category(p.getCategory())
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
