// BranchInventoryRepository.java
package cl.perfulandia.branches.repository;

import cl.perfulandia.branches.model.BranchInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BranchInventoryRepository extends JpaRepository<BranchInventory, Long> {
    List<BranchInventory> findByBranchId(Long branchId);
    Optional<BranchInventory> findByBranchIdAndProductId(Long branchId, Long productId);
    boolean existsByBranchIdAndProductId(Long branchId, Long productId);
}
