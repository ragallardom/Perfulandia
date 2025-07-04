package cl.perfulandia.billing.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceRequest {
    private String customerName;
    private Double amount;
    private String paymentMethod;
}
