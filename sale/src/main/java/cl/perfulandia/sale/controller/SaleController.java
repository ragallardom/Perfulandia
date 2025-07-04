package cl.perfulandia.sale.controller;

import cl.perfulandia.sale.dto.SaleRequest;
import cl.perfulandia.sale.dto.SaleResponse;
import cl.perfulandia.sale.dto.SaleDetailResponse;
import java.util.List;
import cl.perfulandia.sale.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest request) {
        SaleResponse response = saleService.createSale(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branch/{branchId}")
    public List<SaleDetailResponse> getSalesByBranch(@PathVariable Long branchId) {
        return saleService.getSalesByBranch(branchId);
    }
}