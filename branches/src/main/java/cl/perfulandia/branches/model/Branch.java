package cl.perfulandia.branches.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "branches")
@Data
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;
}