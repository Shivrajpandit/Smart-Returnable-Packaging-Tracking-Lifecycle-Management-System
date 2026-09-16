package com.packaging.controller;

import com.packaging.dto.TransactionDTOs.DamageRequest;
import com.packaging.entity.DamageRecord;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/damages")
public class DamageController {

    private final TransactionService transactionService;

    @Autowired
    public DamageController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<DamageRecord>> getAllDamages() {
        return ResponseEntity.ok(transactionService.getAllDamages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DamageRecord> getDamageById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getDamageById(id));
    }

    @PostMapping
    public ResponseEntity<DamageRecord> recordDamage(@Valid @RequestBody DamageRequest request) {
        return new ResponseEntity<>(transactionService.recordDamage(request), HttpStatus.CREATED);
    }
}
