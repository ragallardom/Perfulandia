package cl.perfulandia.branches.controller;

import cl.perfulandia.branches.dto.InventoryRequest;
import cl.perfulandia.branches.dto.InventoryResponse;
import cl.perfulandia.branches.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches/{branchId}/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(
            @PathVariable Long branchId,
            @Valid @RequestBody InventoryRequest req) {
        InventoryResponse created = inventoryService.createInventory(branchId, req);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{inventoryId}")
    public InventoryResponse update(
            @PathVariable Long branchId,
            @PathVariable Long inventoryId,
            @Valid @RequestBody InventoryRequest req) {
        return inventoryService.updateInventory(branchId, inventoryId, req);
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long branchId,
            @PathVariable Long inventoryId) {
        inventoryService.deleteInventory(branchId, inventoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public InventoryResponse getProduct(
            @PathVariable Long branchId,
            @PathVariable Long productId) {
        return inventoryService.getProductInventory(branchId, productId);
    }
}
