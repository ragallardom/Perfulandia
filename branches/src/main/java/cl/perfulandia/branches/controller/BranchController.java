package cl.perfulandia.branches.controller;

import cl.perfulandia.branches.dto.BranchRequest;
import cl.perfulandia.branches.dto.BranchResponse;
import cl.perfulandia.branches.dto.InventoryResponse;
import cl.perfulandia.branches.service.BranchService;
import cl.perfulandia.branches.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;
    private final InventoryService inventoryService;

    public BranchController(BranchService branchService,
                            InventoryService inventoryService) {
        this.branchService = branchService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<BranchResponse> getAll() { return branchService.getAll(); }

    @GetMapping("/{id}")
    public BranchResponse getOne(@PathVariable Long id) { return branchService.getById(id); }

    @PostMapping
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchRequest req) {
        return ResponseEntity.status(201).body(branchService.create(req));
    }

    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable Long id, @Valid @RequestBody BranchRequest req) {
        return branchService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/inventory")
    public List<InventoryResponse> getInventory(@PathVariable Long id) {
        return inventoryService.getInventoryByBranch(id);
    }
}