package cl.perfulandia.logistics.dto;

import lombok.Data;

@Data
public class ShipmentRequest {
    private String orderCode;
    private String origin;
    private String destination;
}
