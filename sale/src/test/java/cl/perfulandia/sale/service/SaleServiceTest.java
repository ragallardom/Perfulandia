package cl.perfulandia.sale.service;

import cl.perfulandia.sale.dto.SaleRequest;
import cl.perfulandia.sale.dto.SaleResponse;
import cl.perfulandia.sale.dto.SaleDetailResponse;
import cl.perfulandia.sale.model.Sale;
import cl.perfulandia.sale.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SaleRepository saleRepository;
    @InjectMocks
    private SaleService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "branchServiceUrl", "http://branch");
        ReflectionTestUtils.setField(service, "catalogServiceUrl", "http://catalog");
    }

    @Test
    void createSaleSuccess() {
        SaleRequest req = new SaleRequest();
        req.setBranchId(1L);
        req.setProductId(2L);
        req.setQuantity(3);
        req.setUserId(4L);

        Map<String, Object> invResp = new HashMap<>();
        invResp.put("stock", 10);
        invResp.put("id", 99);
        when(restTemplate.getForObject(contains("inventory/product"), eq(Map.class))).thenReturn(invResp);
        doNothing().when(restTemplate).put(contains("inventory/"), any());

        Map<String, Object> prodResp = new HashMap<>();
        prodResp.put("price", 5.0);
        when(restTemplate.getForObject(contains("/products/"), eq(Map.class))).thenReturn(prodResp);

        Sale saved = new Sale();
        saved.setId(7L);
        saved.setUnitPrice(5.0);
        saved.setTotalPrice(15.0);
        saved.setTimestamp(LocalDateTime.now());
        when(saleRepository.save(any())).thenReturn(saved);

        SaleResponse resp = service.createSale(req);
        assertEquals(7L, resp.getSaleId());
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void inventoryCallFailureThrows() {
        SaleRequest req = new SaleRequest();
        req.setBranchId(1L);
        req.setProductId(2L);
        req.setQuantity(1);
        req.setUserId(3L);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("fail"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createSale(req));
        assertTrue(ex.getMessage().contains("Error contactando servicio de sucursales"));
    }

    @Test
    void getSalesByBranchReturnsMappedResponses() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setUserId(2L);
        sale.setProductId(3L);
        sale.setBranchId(4L);
        sale.setQuantity(5);
        sale.setUnitPrice(1.5);
        sale.setTotalPrice(7.5);
        sale.setTimestamp(LocalDateTime.now());

        when(saleRepository.findByBranchId(4L)).thenReturn(List.of(sale));

        List<SaleDetailResponse> responses = service.getSalesByBranch(4L);
        assertEquals(1, responses.size());
        assertEquals(3L, responses.get(0).getProductId());
    }
}
