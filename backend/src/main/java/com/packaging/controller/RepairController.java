package com.packaging.controller;

import com.packaging.dto.TransactionDTOs.RepairRequest;
import com.packaging.entity.RepairRecord;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final TransactionService transactionService;

    @Autowired
    public RepairController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<RepairRecord>> getAllRepairs() {
        return ResponseEntity.ok(transactionService.getAllRepairs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairRecord> getRepairById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getRepairById(id));
    }

    @PostMapping
    public ResponseEntity<RepairRecord> createRepair(@Valid @RequestBody RepairRequest request) {
        return new ResponseEntity<>(transactionService.createRepair(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairRecord> updateRepair(@PathVariable Integer id, @Valid @RequestBody RepairRequest request) {
        return ResponseEntity.ok(transactionService.updateRepair(id, request));
    }
}
