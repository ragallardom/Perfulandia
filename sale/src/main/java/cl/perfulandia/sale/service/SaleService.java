package cl.perfulandia.sale.service;

import cl.perfulandia.sale.dto.SaleRequest;
import cl.perfulandia.sale.dto.SaleResponse;
import cl.perfulandia.sale.model.Sale;
import cl.perfulandia.sale.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SaleService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SaleRepository saleRepository;

    private static final String BRANCH_SERVICE_URL = "http://localhost:8082/branches/inventory";
    private static final String CATALOG_SERVICE_URL = "http://localhost:8083/catalog/products";

    public SaleResponse createSale(SaleRequest request) {
        // 1. Check stock
        String inventoryUrl = String.format("%s/%d/%d", BRANCH_SERVICE_URL,
                request.getBranchId(), request.getProductId());
        Map inventoryResponse;
        try {
            inventoryResponse = restTemplate.getForObject(inventoryUrl, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error contacting branch service");
        }
        Integer stock = (Integer) inventoryResponse.get("quantity");
        if (stock == null || stock < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock in branch");
        }

        // 2. Get product price
        String productUrl = String.format("%s/%d", CATALOG_SERVICE_URL, request.getProductId());
        Map productResponse;
        try {
            productResponse = restTemplate.getForObject(productUrl, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error contacting catalog service");
        }
        Double unitPrice = Double.valueOf(productResponse.get("price").toString());

        // 3. Create sale
        Sale sale = new Sale();
        sale.setUserId(request.getUserId());
        sale.setProductId(request.getProductId());
        sale.setBranchId(request.getBranchId());
        sale.setQuantity(request.getQuantity());
        sale.setUnitPrice(unitPrice);
        sale.setTotalPrice(unitPrice * request.getQuantity());
        sale.setTimestamp(LocalDateTime.now());

        Sale saved = saleRepository.save(sale);

        // 4. Build response
        SaleResponse response = new SaleResponse();
        response.setSaleId(saved.getId());
        response.setUnitPrice(saved.getUnitPrice());
        response.setTotalPrice(saved.getTotalPrice());
        response.setTimestamp(saved.getTimestamp());
        return response;
    }
}