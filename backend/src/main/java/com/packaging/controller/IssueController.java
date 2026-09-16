package com.packaging.controller;

import com.packaging.dto.TransactionDTOs.IssueRequest;
import com.packaging.entity.IssueTransaction;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    private final TransactionService transactionService;

    @Autowired
    public IssueController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<IssueTransaction>> getAllIssues() {
        return ResponseEntity.ok(transactionService.getAllIssues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueTransaction> getIssueById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getIssueById(id));
    }

    @PostMapping
    public ResponseEntity<IssueTransaction> issueAsset(@Valid @RequestBody IssueRequest request) {
        return new ResponseEntity<>(transactionService.issueAsset(request), HttpStatus.CREATED);
    }
}
