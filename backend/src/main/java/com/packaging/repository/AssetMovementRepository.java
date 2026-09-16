package com.packaging.repository;

import com.packaging.entity.AssetMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetMovementRepository extends JpaRepository<AssetMovement, Integer> {
    List<AssetMovement> findByAssetAssetIdOrderByMovementDateDesc(Integer assetId);
    List<AssetMovement> findAllByOrderByMovementDateDesc();
}
