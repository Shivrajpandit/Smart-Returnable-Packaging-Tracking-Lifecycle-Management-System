package com.packaging.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ReportDTO {

    public static class DashboardMetrics {
        private long totalAssets;
        private long availableAssets;
        private long issuedAssets;
        private long damagedAssets;
        private long underRepairAssets;
        private long lostAssets;
        private long retiredAssets;
        private long overdueReturns;
        private long totalCustomers;
        private BigDecimal totalRepairCost;

        public DashboardMetrics() {}

        public long getTotalAssets() { return totalAssets; }
        public void setTotalAssets(long totalAssets) { this.totalAssets = totalAssets; }
        public long getAvailableAssets() { return availableAssets; }
        public void setAvailableAssets(long availableAssets) { this.availableAssets = availableAssets; }
        public long getIssuedAssets() { return issuedAssets; }
        public void setIssuedAssets(long issuedAssets) { this.issuedAssets = issuedAssets; }
        public long getDamagedAssets() { return damagedAssets; }
        public void setDamagedAssets(long damagedAssets) { this.damagedAssets = damagedAssets; }
        public long getUnderRepairAssets() { return underRepairAssets; }
        public void setUnderRepairAssets(long underRepairAssets) { this.underRepairAssets = underRepairAssets; }
        public long getLostAssets() { return lostAssets; }
        public void setLostAssets(long lostAssets) { this.lostAssets = lostAssets; }
        public long getRetiredAssets() { return retiredAssets; }
        public void setRetiredAssets(long retiredAssets) { this.retiredAssets = retiredAssets; }
        public long getOverdueReturns() { return overdueReturns; }
        public void setOverdueReturns(long overdueReturns) { this.overdueReturns = overdueReturns; }
        public long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
        public BigDecimal getTotalRepairCost() { return totalRepairCost; }
        public void setTotalRepairCost(BigDecimal totalRepairCost) { this.totalRepairCost = totalRepairCost; }
    }

    public static class OverdueItem {
        private Integer issueId;
        private Integer assetId;
        private String assetCode;
        private String packagingTypeName;
        private String customerName;
        private String customerPhone;
        private LocalDate issueDate;
        private LocalDate expectedReturnDate;
        private long daysOverdue;

        public OverdueItem() {}

        public Integer getIssueId() { return issueId; }
        public void setIssueId(Integer issueId) { this.issueId = issueId; }
        public Integer getAssetId() { return assetId; }
        public void setAssetId(Integer assetId) { this.assetId = assetId; }
        public String getAssetCode() { return assetCode; }
        public void setAssetCode(String assetCode) { this.assetCode = assetCode; }
        public String getPackagingTypeName() { return packagingTypeName; }
        public void setPackagingTypeName(String packagingTypeName) { this.packagingTypeName = packagingTypeName; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
        public LocalDate getIssueDate() { return issueDate; }
        public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
        public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
        public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
        public long getDaysOverdue() { return daysOverdue; }
        public void setDaysOverdue(long daysOverdue) { this.daysOverdue = daysOverdue; }
    }

    public static class CustomerOutstandingItem {
        private Integer customerId;
        private String companyName;
        private String contactPerson;
        private String phone;
        private long totalIssued;
        private long totalReturned;
        private long currentlyOutstanding;

        public CustomerOutstandingItem() {}

        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public long getTotalIssued() { return totalIssued; }
        public void setTotalIssued(long totalIssued) { this.totalIssued = totalIssued; }
        public long getTotalReturned() { return totalReturned; }
        public void setTotalReturned(long totalReturned) { this.totalReturned = totalReturned; }
        public long getCurrentlyOutstanding() { return currentlyOutstanding; }
        public void setCurrentlyOutstanding(long currentlyOutstanding) { this.currentlyOutstanding = currentlyOutstanding; }
    }
}
