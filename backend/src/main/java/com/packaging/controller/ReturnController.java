package com.packaging.controller;

import com.packaging.dto.TransactionDTOs.ReturnRequest;
import com.packaging.entity.ReturnTransaction;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    private final TransactionService transactionService;

    @Autowired
    public ReturnController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<ReturnTransaction>> getAllReturns() {
        return ResponseEntity.ok(transactionService.getAllReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnTransaction> getReturnById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getReturnById(id));
    }

    @PostMapping
    public ResponseEntity<ReturnTransaction> returnAsset(@Valid @RequestBody ReturnRequest request) {
        return new ResponseEntity<>(transactionService.returnAsset(request), HttpStatus.CREATED);
    }
}
