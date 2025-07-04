package cl.perfulandia.logistics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipmentResponse {
    private Long id;
    private String orderCode;
    private String currentStatus;
    private String origin;
    private String destination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
