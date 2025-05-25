package cl.perfulandia.sale.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter
@Setter

public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "branchId", nullable = false)
    private Long branchId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false)
    private Double unitPrice;

    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}

