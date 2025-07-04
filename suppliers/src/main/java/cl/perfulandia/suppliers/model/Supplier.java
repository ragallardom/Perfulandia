package cl.perfulandia.suppliers.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String taxId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String productType;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<ReplenishmentOrder> orders;

    public Supplier(String taxId, String name, String address, String phone, String email, String productType) {
        this.taxId = taxId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.productType = productType;
    }
}
