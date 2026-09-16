package com.packaging.repository;

import com.packaging.entity.Asset;
import com.packaging.entity.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {

    Optional<Asset> findByAssetCode(String assetCode);

    boolean existsByAssetCode(String assetCode);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByWarehouseWarehouseId(Integer warehouseId);

    List<Asset> findByPackagingTypeTypeId(Integer typeId);

    long countByStatus(AssetStatus status);

    @Query("SELECT a FROM Asset a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:typeId IS NULL OR a.packagingType.typeId = :typeId) AND " +
           "(:warehouseId IS NULL OR a.warehouse.warehouseId = :warehouseId) AND " +
           "(:query IS NULL OR :query = '' OR LOWER(a.assetCode) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Asset> filterAssets(
            @Param("status") AssetStatus status,
            @Param("typeId") Integer typeId,
            @Param("warehouseId") Integer warehouseId,
            @Param("query") String query
    );
}
