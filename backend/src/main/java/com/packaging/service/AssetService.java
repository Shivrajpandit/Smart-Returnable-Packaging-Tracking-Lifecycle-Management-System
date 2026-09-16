package com.packaging.service;

import com.packaging.dto.AssetDTO;
import com.packaging.entity.Asset;
import com.packaging.entity.AssetStatus;
import com.packaging.entity.PackagingType;
import com.packaging.entity.Warehouse;
import com.packaging.exception.ConflictException;
import com.packaging.exception.ResourceNotFoundException;
import com.packaging.repository.AssetRepository;
import com.packaging.repository.PackagingTypeRepository;
import com.packaging.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final PackagingTypeRepository packagingTypeRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository,
                        PackagingTypeRepository packagingTypeRepository,
                        WarehouseRepository warehouseRepository) {
        this.assetRepository = assetRepository;
        this.packagingTypeRepository = packagingTypeRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> filterAssets(AssetStatus status, Integer typeId, Integer warehouseId, String query) {
        return assetRepository.filterAssets(status, typeId, warehouseId, query != null ? query.trim() : null);
    }

    public Asset getAssetById(Integer assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));
    }

    public Asset getAssetByCode(String assetCode) {
        return assetRepository.findByAssetCode(assetCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with code: " + assetCode));
    }

    @Transactional
    public Asset createAsset(AssetDTO dto) {
        String cleanCode = dto.getAssetCode().trim().toUpperCase();
        if (assetRepository.existsByAssetCode(cleanCode)) {
            throw new ConflictException("Asset code '" + cleanCode + "' already exists");
        }

        PackagingType type = packagingTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Packaging type not found with id: " + dto.getTypeId()));

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        Asset asset = new Asset();
        asset.setAssetCode(cleanCode);
        asset.setPackagingType(type);
        asset.setWarehouse(warehouse);
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setStatus(dto.getStatus() != null ? dto.getStatus() : AssetStatus.AVAILABLE);

        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateAsset(Integer assetId, AssetDTO dto) {
        Asset asset = getAssetById(assetId);

        PackagingType type = packagingTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Packaging type not found with id: " + dto.getTypeId()));

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        asset.setPackagingType(type);
        asset.setWarehouse(warehouse);
        asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getStatus() != null) {
            asset.setStatus(dto.getStatus());
        }

        return assetRepository.save(asset);
    }

    @Transactional
    public void deleteAsset(Integer assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Asset not found with id: " + assetId);
        }
        assetRepository.deleteById(assetId);
    }
}
