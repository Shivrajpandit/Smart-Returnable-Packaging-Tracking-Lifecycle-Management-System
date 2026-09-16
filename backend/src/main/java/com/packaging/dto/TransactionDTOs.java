package com.packaging.dto;

import com.packaging.entity.ConditionStatus;
import com.packaging.entity.RepairStatus;
import com.packaging.entity.Severity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTOs {

    public static class IssueRequest {
        @NotNull(message = "Asset is required")
        private Integer assetId;

        @NotNull(message = "Customer is required")
        private Integer customerId;

        @NotNull(message = "Issuing user is required")
        private Integer issuedBy;

        @NotNull(message = "Issue date is required")
        private LocalDate issueDate;

        @NotNull(message = "Expected return date is required")
        private LocalDate expectedReturnDate;

        private String purpose;

        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        public Integer getIssuedBy() { return issuedBy; }
        public void setIssuedBy(Integer issuedBy) { this.issuedBy = issuedBy; }
        public LocalDate getIssueDate() { return issueDate; }
        public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
        public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
        public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
        public String getPurpose() { return purpose; }
        public void setPurpose(String purpose) { this.purpose = purpose; }
    }

    public static class ReturnRequest {
        @NotNull(message = "Asset is required")
        private Integer assetId;

        @NotNull(message = "Customer is required")
        private Integer customerId;

        @NotNull(message = "Receiving staff user is required")
        private Integer receivedBy;

        @NotNull(message = "Return date is required")
        private LocalDate returnDate;

        @NotNull(message = "Condition status is required")
        private ConditionStatus conditionStatus;

        private String remarks;

        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        public Integer getReceivedBy() { return receivedBy; }
        public void setReceivedBy(Integer receivedBy) { this.receivedBy = receivedBy; }
        public LocalDate getReturnDate() { return returnDate; }
        public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
        public ConditionStatus getConditionStatus() { return conditionStatus; }
        public void setConditionStatus(ConditionStatus conditionStatus) { this.conditionStatus = conditionStatus; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class DamageRequest {
        @NotNull(message = "Asset is required")
        private Integer assetId;

        @NotNull(message = "Reporting user is required")
        private Integer reportedBy;

        @NotBlank(message = "Damage type is required")
        private String damageType;

        @NotNull(message = "Severity is required")
        private Severity severity;

        @NotNull(message = "Damage date is required")
        private LocalDate damageDate;

        @DecimalMin(value = "0.00", message = "Estimated cost cannot be negative")
        private BigDecimal estimatedCost = BigDecimal.ZERO;

        private String description;

        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public Integer getReportedBy() { return reportedBy; }
        public void setReportedBy(Integer reportedBy) { this.reportedBy = reportedBy; }
        public String getDamageType() { return damageType; }
        public void setDamageType(String damageType) { this.damageType = damageType; }
        public Severity getSeverity() { return severity; }
        public void setSeverity(Severity severity) { this.severity = severity; }
        public LocalDate getDamageDate() { return damageDate; }
        public void setDamageDate(LocalDate damageDate) { this.damageDate = damageDate; }
        public BigDecimal getEstimatedCost() { return estimatedCost; }
        public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class RepairRequest {
        @NotNull(message = "Asset is required")
        private Integer assetId;

        private Integer damageId;

        @NotNull(message = "Repair staff user is required")
        private Integer repairedBy;

        private LocalDate repairDate;

        @DecimalMin(value = "0.00", message = "Repair cost cannot be negative")
        private BigDecimal repairCost = BigDecimal.ZERO;

        @NotNull(message = "Repair status is required")
        private RepairStatus repairStatus;

        private String remarks;

        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public Integer getDamageId() { return damageId; }
        public void setDamageId(Integer damageId) { this.damageId = damageId; }
        public Integer getRepairedBy() { return repairedBy; }
        public void setRepairedBy(Integer repairedBy) { this.repairedBy = repairedBy; }
        public LocalDate getRepairDate() { return repairDate; }
        public void setRepairDate(LocalDate repairDate) { this.repairDate = repairDate; }
        public BigDecimal getRepairCost() { return repairCost; }
        public void setRepairCost(BigDecimal repairCost) { this.repairCost = repairCost; }
        public RepairStatus getRepairStatus() { return repairStatus; }
        public void setRepairStatus(RepairStatus repairStatus) { this.repairStatus = repairStatus; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class MovementRequest {
        @NotNull(message = "Asset is required")
        private Integer assetId;

        @NotBlank(message = "From location is required")
        private String fromLocation;

        @NotBlank(message = "To location is required")
        private String toLocation;

        @NotNull(message = "Moved by staff user is required")
        private Integer movedBy;

        private String remarks;

        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public String getFromLocation() { return fromLocation; }
        public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }
        public String getToLocation() { return toLocation; }
        public void setToLocation(String toLocation) { this.toLocation = toLocation; }
        public Integer getMovedBy() { return movedBy; }
        public void setMovedBy(Integer movedBy) { this.movedBy = movedBy; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }
}
