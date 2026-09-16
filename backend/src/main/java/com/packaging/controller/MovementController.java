package com.packaging.controller;

import com.packaging.dto.TransactionDTOs.MovementRequest;
import com.packaging.entity.AssetMovement;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final TransactionService transactionService;

    @Autowired
    public MovementController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<AssetMovement>> getAllMovements() {
        return ResponseEntity.ok(transactionService.getAllMovements());
    }

    @PostMapping
    public ResponseEntity<AssetMovement> recordMovement(@Valid @RequestBody MovementRequest request) {
        return new ResponseEntity<>(transactionService.recordMovement(request), HttpStatus.CREATED);
    }
}
