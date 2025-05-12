package cl.perfulandia.branches.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "branch_inventory", uniqueConstraints = @UniqueConstraint(
        columnNames = {"branch_id","product_id"}
))
@Data
public class BranchInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer stock;
}