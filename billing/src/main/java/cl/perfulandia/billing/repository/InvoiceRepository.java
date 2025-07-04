package cl.perfulandia.billing.repository;

import cl.perfulandia.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {}