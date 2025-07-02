package cl.perfulandia.branches.service;

import cl.perfulandia.branches.dto.BranchRequest;
import cl.perfulandia.branches.dto.BranchResponse;
import cl.perfulandia.branches.model.Branch;
import cl.perfulandia.branches.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchServiceTest {
    @Mock
    private BranchRepository repo;
    @InjectMocks
    private BranchService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReturnsMappedResponses() {
        Branch b = new Branch();
        b.setId(1L);
        b.setName("Main");
        b.setAddress("Street 1");
        when(repo.findAll()).thenReturn(List.of(b));

        List<BranchResponse> resps = service.getAll();
        assertEquals(1, resps.size());
        assertEquals("Main", resps.get(0).getName());
    }

    @Test
    void getByIdFound() {
        Branch b = new Branch();
        b.setId(1L);
        b.setName("Main");
        b.setAddress("Street 1");
        when(repo.findById(1L)).thenReturn(Optional.of(b));

        BranchResponse resp = service.getById(1L);
        assertEquals("Main", resp.getName());
    }

    @Test
    void createSavesAndReturnsResponse() {
        BranchRequest req = new BranchRequest();
        req.setName("New");
        req.setAddress("Addr");

        Branch toSave = new Branch();
        toSave.setName("New");
        toSave.setAddress("Addr");

        Branch saved = new Branch();
        saved.setId(5L);
        saved.setName("New");
        saved.setAddress("Addr");
        when(repo.save(any())).thenReturn(saved);

        BranchResponse resp = service.create(req);
        assertEquals(5L, resp.getId());
        verify(repo).save(any(Branch.class));
    }

    @Test
    void updateModifiesExisting() {
        BranchRequest req = new BranchRequest();
        req.setName("Up");
        req.setAddress("Addr");

        Branch existing = new Branch();
        existing.setId(2L);
        existing.setName("Old");
        existing.setAddress("old");
        when(repo.findById(2L)).thenReturn(Optional.of(existing));

        Branch saved = new Branch();
        saved.setId(2L);
        saved.setName("Up");
        saved.setAddress("Addr");
        when(repo.save(existing)).thenReturn(saved);

        BranchResponse resp = service.update(2L, req);
        assertEquals("Up", resp.getName());
    }

    @Test
    void deleteDelegatesToRepository() {
        service.delete(3L);
        verify(repo).deleteById(3L);
    }
}
