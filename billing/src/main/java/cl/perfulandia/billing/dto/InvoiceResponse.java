package cl.perfulandia.billing.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceResponse {
    private Long id;
    private String customerName;
    private Double amount;
    private LocalDateTime dateIssued;
    private Boolean paid;
    private String paymentMethod;
}
