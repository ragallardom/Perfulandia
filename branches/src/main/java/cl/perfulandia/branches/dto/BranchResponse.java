package cl.perfulandia.branches.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchResponse {
    private Long id;
    private String name;
    private String address;
}