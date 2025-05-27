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
import java.util.HashMap;
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
        String getInventoryUrl = branchServiceUrl
                + "/branches/" + request.getBranchId()
                + "/inventory/product/" + request.getProductId();

        Map<String, Object> inventoryResponse;
        try {
            inventoryResponse = restTemplate.getForObject(getInventoryUrl, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error contactando servicio de sucursales: " + e.getMessage());
        }

        if (inventoryResponse == null || !inventoryResponse.containsKey("stock") || !inventoryResponse.containsKey("id")) {
            throw new RuntimeException("Respuesta inválida al consultar inventario");
        }

        Integer currentStock = (Integer) inventoryResponse.get("stock");
        Long inventoryId    = ((Number) inventoryResponse.get("id")).longValue();

        if (currentStock < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente en la sucursal para el producto ID "
                    + request.getProductId());
        }

        Integer newStock = currentStock - request.getQuantity();

        String updateInventoryUrl = branchServiceUrl
                + "/branches/" + request.getBranchId()
                + "/inventory/" + inventoryId;

        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("productId", request.getProductId());
        updateBody.put("stock", newStock);

        try {
            restTemplate.put(updateInventoryUrl, updateBody);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar stock en sucursal: " + e.getMessage());
        }

        String productUrl = catalogServiceUrl + "/products/" + request.getProductId();
        Map<String, Object> productResponse;
        try {
            productResponse = restTemplate.getForObject(productUrl, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error contactando servicio de catálogo: " + e.getMessage());
        }
        if (productResponse == null || !productResponse.containsKey("price")) {
            throw new RuntimeException("Respuesta inválida al consultar precio de producto");
        }

        Double unitPrice = ((Number) productResponse.get("price")).doubleValue();

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
