package com.packaging.dto;

import com.packaging.entity.*;
import java.util.List;

public class AssetHistoryDTO {
    private Asset asset;
    private List<IssueTransaction> issueHistory;
    private List<ReturnTransaction> returnHistory;
    private List<DamageRecord> damageHistory;
    private List<RepairRecord> repairHistory;
    private List<AssetMovement> movementHistory;

    public AssetHistoryDTO() {}

    public AssetHistoryDTO(Asset asset, List<IssueTransaction> issueHistory, List<ReturnTransaction> returnHistory, List<DamageRecord> damageHistory, List<RepairRecord> repairHistory, List<AssetMovement> movementHistory) {
        this.asset = asset;
        this.issueHistory = issueHistory;
        this.returnHistory = returnHistory;
        this.damageHistory = damageHistory;
        this.repairHistory = repairHistory;
        this.movementHistory = movementHistory;
    }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public List<IssueTransaction> getIssueHistory() { return issueHistory; }
    public void setIssueHistory(List<IssueTransaction> issueHistory) { this.issueHistory = issueHistory; }
    public List<ReturnTransaction> getReturnHistory() { return returnHistory; }
    public void setReturnHistory(List<ReturnTransaction> returnHistory) { this.returnHistory = returnHistory; }
    public List<DamageRecord> getDamageHistory() { return damageHistory; }
    public void setDamageHistory(List<DamageRecord> damageHistory) { this.damageHistory = damageHistory; }
    public List<RepairRecord> getRepairHistory() { return repairHistory; }
    public void setRepairHistory(List<RepairRecord> repairHistory) { this.repairHistory = repairHistory; }
    public List<AssetMovement> getMovementHistory() { return movementHistory; }
    public void setMovementHistory(List<AssetMovement> movementHistory) { this.movementHistory = movementHistory; }
}
