package cl.perfulandia.branches.repository;

import cl.perfulandia.branches.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {}