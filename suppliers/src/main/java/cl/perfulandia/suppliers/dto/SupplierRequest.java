package cl.perfulandia.suppliers.dto;

import lombok.Data;

@Data
public class SupplierRequest {
    private String taxId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String productType;
}
