package cl.perfulandia.branches.service;

import cl.perfulandia.branches.config.ResourceNotFoundException;
import cl.perfulandia.branches.dto.BranchRequest;
import cl.perfulandia.branches.dto.BranchResponse;
import cl.perfulandia.branches.model.Branch;
import cl.perfulandia.branches.repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {
    private final BranchRepository repo;

    public BranchService(BranchRepository repo) {
        this.repo = repo;
    }

    public List<BranchResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(b -> new BranchResponse(b.getId(), b.getName(), b.getAddress()))
                .collect(Collectors.toList());
    }

    public BranchResponse getById(Long id) {
        Branch b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        return new BranchResponse(b.getId(), b.getName(), b.getAddress());
    }

    public BranchResponse create(BranchRequest req) {
        Branch b = new Branch();
        b.setName(req.getName());
        b.setAddress(req.getAddress());
        Branch saved = repo.save(b);
        return new BranchResponse(saved.getId(), saved.getName(), saved.getAddress());
    }

    public BranchResponse update(Long id, BranchRequest req) {
        Branch b = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        b.setName(req.getName());
        b.setAddress(req.getAddress());
        Branch updated = repo.save(b);
        return new BranchResponse(updated.getId(), updated.getName(), updated.getAddress());
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}