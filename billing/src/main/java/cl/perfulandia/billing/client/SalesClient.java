package cl.perfulandia.billing.client;

import cl.perfulandia.billing.dto.SaleDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SalesClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public SaleDTO getSaleById(Long saleId) {
        return restTemplate.getForObject("http://localhost:8082/api/sales/" + saleId, SaleDTO.class);
    }
}