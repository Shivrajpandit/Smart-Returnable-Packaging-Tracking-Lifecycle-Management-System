package com.packaging.controller;

import com.packaging.dto.AssetDTO;
import com.packaging.dto.AssetHistoryDTO;
import com.packaging.entity.Asset;
import com.packaging.entity.AssetStatus;
import com.packaging.service.AssetService;
import com.packaging.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    private final AssetService assetService;
    private final TransactionService transactionService;

    @Autowired
    public AssetController(AssetService assetService, TransactionService transactionService) {
        this.assetService = assetService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAssets(
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Integer warehouseId,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(assetService.filterAssets(status, typeId, warehouseId, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Integer id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Asset> getAssetByCode(@PathVariable String code) {
        return ResponseEntity.ok(assetService.getAssetByCode(code));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<AssetHistoryDTO> getAssetHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getAssetHistory(id));
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetDTO dto) {
        return new ResponseEntity<>(assetService.createAsset(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Integer id, @Valid @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Integer id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
