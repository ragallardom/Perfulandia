package cl.perfulandia.sale.service;

import cl.perfulandia.sale.dto.SaleRequest;
import cl.perfulandia.sale.dto.SaleResponse;
import cl.perfulandia.sale.model.Sale;
import cl.perfulandia.sale.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SaleService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SaleRepository saleRepository;

    @Value("${branch.service.url}")
    private String branchServiceUrl;

    @Value("${catalog.service.url}")
    private String catalogServiceUrl;

    public SaleResponse createSale(SaleRequest request) {
        String inventoryUrl = String.format("%s/branches/%d/inventory/product/%d",
                branchServiceUrl, request.getBranchId(), request.getProductId());

        Map<String, Object> inventoryResponse;
        try {
            inventoryResponse = restTemplate.getForObject(inventoryUrl, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error contacting branch service: " + e.getMessage());
        }

        if (inventoryResponse == null || !inventoryResponse.containsKey("stock")) {
            throw new RuntimeException("Invalid response from branch service");
        }

        Integer stock = (Integer) inventoryResponse.get("stock");
        if (stock == null || stock < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock in branch");
        }

        String productUrl = String.format("%s/products/%d", catalogServiceUrl, request.getProductId());

        Map<String, Object> productResponse;
        try {
            productResponse = restTemplate.getForObject(productUrl, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error contacting catalog service: " + e.getMessage());
        }

        if (productResponse == null || !productResponse.containsKey("price")) {
            throw new RuntimeException("Invalid response from catalog service");
        }

        Double unitPrice;
        try {
            unitPrice = Double.valueOf(productResponse.get("price").toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid price format from catalog service");
        }

        Sale sale = new Sale();
        sale.setUserId(request.getUserId());
        sale.setProductId(request.getProductId());
        sale.setBranchId(request.getBranchId());
        sale.setQuantity(request.getQuantity());
        sale.setUnitPrice(unitPrice);
        sale.setTotalPrice(unitPrice * request.getQuantity());
        sale.setTimestamp(LocalDateTime.now());

        Sale saved = saleRepository.save(sale);

        SaleResponse response = new SaleResponse();
        response.setSaleId(saved.getId());
        response.setUnitPrice(saved.getUnitPrice());
        response.setTotalPrice(saved.getTotalPrice());
        response.setTimestamp(saved.getTimestamp());

        return response;
    }
}
