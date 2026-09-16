package com.packaging.repository;

import com.packaging.entity.RepairRecord;
import com.packaging.entity.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RepairRecordRepository extends JpaRepository<RepairRecord, Integer> {

    List<RepairRecord> findByAssetAssetIdOrderByCreatedAtDesc(Integer assetId);

    List<RepairRecord> findByRepairStatus(RepairStatus repairStatus);

    @Query("SELECT COALESCE(SUM(r.repairCost), 0) FROM RepairRecord r")
    BigDecimal sumTotalRepairCost();
}
