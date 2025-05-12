package cl.perfulandia.branches.service;

import cl.perfulandia.branches.config.ResourceNotFoundException;
import cl.perfulandia.branches.dto.InventoryRequest;
import cl.perfulandia.branches.dto.InventoryResponse;
import cl.perfulandia.branches.dto.ProductDto;
import cl.perfulandia.branches.model.Branch;
import cl.perfulandia.branches.model.BranchInventory;
import cl.perfulandia.branches.repository.BranchInventoryRepository;
import cl.perfulandia.branches.repository.BranchRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final BranchInventoryRepository invRepo;
    private final BranchRepository branchRepo;
    private final RestTemplate restTemplate;

    @Value("${catalog.service.url}")
    private String catalogUrl;

    public InventoryService(BranchInventoryRepository invRepo,
                            BranchRepository branchRepo,
                            RestTemplate restTemplate) {
        this.invRepo = invRepo;
        this.branchRepo = branchRepo;
        this.restTemplate = restTemplate;
    }

    public List<InventoryResponse> getInventoryByBranch(Long branchId) {
        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Llama al catálogo con RestTemplate
        ResponseEntity<List<ProductDto>> resp = restTemplate.exchange(
                catalogUrl + "/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<ProductDto> products = resp.getBody();

        Map<Long, String> productNames = products.stream()
                .collect(Collectors.toMap(ProductDto::getId, ProductDto::getName));

        return invRepo.findByBranchId(branchId).stream()
                .map(inv -> new InventoryResponse(
                        inv.getId(),
                        branch.getName(),
                        productNames.get(inv.getProductId()),
                        inv.getStock()))
                .collect(Collectors.toList());
    }

    public InventoryResponse createInventory(Long branchId, @Valid InventoryRequest req) {
        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        if (invRepo.existsByBranchIdAndProductId(branchId, req.getProductId())) {
            throw new IllegalArgumentException("Inventory entry already exists");
        }
        BranchInventory inv = new BranchInventory();
        inv.setBranchId(branchId);
        inv.setProductId(req.getProductId());
        inv.setStock(req.getStock());
        BranchInventory saved = invRepo.save(inv);
        String productName = fetchProductName(req.getProductId());
        return new InventoryResponse(saved.getId(), branch.getName(), productName, saved.getStock());
    }

    private String fetchProductName(@NotNull Long productId) {
        ResponseEntity<ProductDto> resp = restTemplate.getForEntity(
                catalogUrl + "/products/" + productId,
                ProductDto.class
        );
        if (resp.getStatusCode().is2xxSuccessful()) {
            return resp.getBody().getName();
        } else {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    public InventoryResponse updateInventory(Long branchId, Long invId, InventoryRequest req) {
        BranchInventory inv = invRepo.findById(invId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        if (!inv.getBranchId().equals(branchId)) {
            throw new IllegalArgumentException("Inventory does not belong to branch");
        }
        inv.setStock(req.getStock());
        BranchInventory updated = invRepo.save(inv);
        String productName = fetchProductName(updated.getProductId());
        String branchName  = branchRepo.findById(branchId).get().getName();
        return new InventoryResponse(updated.getId(), branchName, productName, updated.getStock());
    }

    public void deleteInventory(Long branchId, Long invId) {
        BranchInventory inv = invRepo.findById(invId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        if (!inv.getBranchId().equals(branchId)) {
            throw new IllegalArgumentException("Inventory does not belong to branch");
        }
        invRepo.delete(inv);
    }

}
