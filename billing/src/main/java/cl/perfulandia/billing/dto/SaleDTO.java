package cl.perfulandia.billing.dto;

import java.util.List;

public class SaleDTO {
    private Long id;
    private String customerName;
    private Double amount;
    private List<String> products;
    private String status;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public List<String> getProducts() { return products; }
    public void setProducts(List<String> products) { this.products = products; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}