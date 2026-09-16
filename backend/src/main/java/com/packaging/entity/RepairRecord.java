package com.packaging.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_records")
public class RepairRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repair_id")
    private Integer repairId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "damage_id")
    private DamageRecord damageRecord;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repaired_by", nullable = false)
    private User repairedBy;

    @Column(name = "repair_date")
    private LocalDate repairDate;

    @Column(name = "repair_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal repairCost = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "repair_status", nullable = false)
    private RepairStatus repairStatus = RepairStatus.PENDING;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public RepairRecord() {}

    public RepairRecord(Integer repairId, Asset asset, DamageRecord damageRecord, User repairedBy, LocalDate repairDate, BigDecimal repairCost, RepairStatus repairStatus, String remarks) {
        this.repairId = repairId;
        this.asset = asset;
        this.damageRecord = damageRecord;
        this.repairedBy = repairedBy;
        this.repairDate = repairDate;
        this.repairCost = repairCost;
        this.repairStatus = repairStatus;
        this.remarks = remarks;
    }

    public Integer getRepairId() { return repairId; }
    public void setRepairId(Integer repairId) { this.repairId = repairId; }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }

    public DamageRecord getDamageRecord() { return damageRecord; }
    public void setDamageRecord(DamageRecord damageRecord) { this.damageRecord = damageRecord; }

    public User getRepairedBy() { return repairedBy; }
    public void setRepairedBy(User repairedBy) { this.repairedBy = repairedBy; }

    public LocalDate getRepairDate() { return repairDate; }
    public void setRepairDate(LocalDate repairDate) { this.repairDate = repairDate; }

    public BigDecimal getRepairCost() { return repairCost; }
    public void setRepairCost(BigDecimal repairCost) { this.repairCost = repairCost; }

    public RepairStatus getRepairStatus() { return repairStatus; }
    public void setRepairStatus(RepairStatus repairStatus) { this.repairStatus = repairStatus; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
