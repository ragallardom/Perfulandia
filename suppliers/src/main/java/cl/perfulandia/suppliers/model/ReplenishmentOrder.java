package cl.perfulandia.suppliers.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "replenishment_orders")
public class ReplenishmentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Supplier proveedor;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    public enum Estado {
        PENDIENTE,
        ENVIADO,
        RECIBIDO,
        CANCELADO
    }
}
