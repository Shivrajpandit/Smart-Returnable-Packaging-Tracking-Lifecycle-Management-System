package com.packaging.repository;

import com.packaging.entity.DamageRecord;
import com.packaging.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageRecordRepository extends JpaRepository<DamageRecord, Integer> {
    List<DamageRecord> findByAssetAssetIdOrderByDamageDateDesc(Integer assetId);
    List<DamageRecord> findBySeverity(Severity severity);
}
