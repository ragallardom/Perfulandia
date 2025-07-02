package cl.perfulandia.branches.service;

import cl.perfulandia.branches.config.ResourceNotFoundException;
import cl.perfulandia.branches.dto.InventoryRequest;
import cl.perfulandia.branches.dto.InventoryResponse;
import cl.perfulandia.branches.dto.ProductDto;
import cl.perfulandia.branches.model.Branch;
import cl.perfulandia.branches.model.BranchInventory;
import cl.perfulandia.branches.repository.BranchInventoryRepository;
import cl.perfulandia.branches.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private BranchInventoryRepository invRepo;
    @Mock
    private BranchRepository branchRepo;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private InventoryService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "catalogUrl", "http://cat");
    }

    @Test
    void getInventoryByBranchReturnsResponses() {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("B1");
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));

        BranchInventory inv = new BranchInventory();
        inv.setId(10L);
        inv.setBranchId(1L);
        inv.setProductId(5L);
        inv.setStock(3);
        when(invRepo.findByBranchId(1L)).thenReturn(List.of(inv));

        ProductDto dto = ProductDto.builder().id(5L).name("Prod").build();
        ResponseEntity<List<ProductDto>> resp = ResponseEntity.ok(List.of(dto));
        when(restTemplate.exchange(eq("http://cat/products"), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(resp);

        List<InventoryResponse> responses = service.getInventoryByBranch(1L);
        assertEquals(1, responses.size());
        assertEquals("Prod", responses.get(0).getProductName());
    }

    @Test
    void createInventoryThrowsWhenExists() {
        when(branchRepo.findById(1L)).thenReturn(Optional.of(new Branch()));
        when(invRepo.existsByBranchIdAndProductId(1L, 2L)).thenReturn(true);
        InventoryRequest req = new InventoryRequest();
        req.setProductId(2L);
        req.setStock(1);
        assertThrows(IllegalArgumentException.class, () -> service.createInventory(1L, req));
    }

    @Test
    void deleteInventoryValidatesBranch() {
        BranchInventory inv = new BranchInventory();
        inv.setId(3L);
        inv.setBranchId(1L);
        when(invRepo.findById(3L)).thenReturn(Optional.of(inv));

        service.deleteInventory(1L, 3L);
        verify(invRepo).delete(inv);
    }

    @Test
    void deleteInventoryWrongBranch() {
        BranchInventory inv = new BranchInventory();
        inv.setId(3L);
        inv.setBranchId(2L);
        when(invRepo.findById(3L)).thenReturn(Optional.of(inv));

        assertThrows(IllegalArgumentException.class, () -> service.deleteInventory(1L, 3L));
    }
}
