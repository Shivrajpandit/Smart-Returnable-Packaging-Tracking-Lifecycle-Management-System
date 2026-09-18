package com.packaging.service;

import com.packaging.dto.ReportDTO.*;
import com.packaging.entity.AssetStatus;
import com.packaging.entity.Customer;
import com.packaging.entity.IssueTransaction;
import com.packaging.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final IssueTransactionRepository issueRepository;
    private final ReturnTransactionRepository returnRepository;
    private final RepairRecordRepository repairRepository;

    @Autowired
    public ReportService(AssetRepository assetRepository,
                         CustomerRepository customerRepository,
                         IssueTransactionRepository issueRepository,
                         ReturnTransactionRepository returnRepository,
                         RepairRecordRepository repairRepository) {
        this.assetRepository = assetRepository;
        this.customerRepository = customerRepository;
        this.issueRepository = issueRepository;
        this.returnRepository = returnRepository;
        this.repairRepository = repairRepository;
    }

    public DashboardMetrics getDashboardMetrics() {
        DashboardMetrics metrics = new DashboardMetrics();
        metrics.setTotalAssets(assetRepository.count());
        metrics.setAvailableAssets(assetRepository.countByStatus(AssetStatus.AVAILABLE));
        metrics.setIssuedAssets(assetRepository.countByStatus(AssetStatus.ISSUED));
        metrics.setDamagedAssets(assetRepository.countByStatus(AssetStatus.DAMAGED));
        metrics.setUnderRepairAssets(assetRepository.countByStatus(AssetStatus.UNDER_REPAIR));
        metrics.setLostAssets(assetRepository.countByStatus(AssetStatus.LOST));
        metrics.setRetiredAssets(assetRepository.countByStatus(AssetStatus.RETIRED));
        metrics.setOverdueReturns(issueRepository.countOverdueIssues(LocalDate.now()));
        metrics.setTotalCustomers(customerRepository.count());
        metrics.setTotalRepairCost(repairRepository.sumTotalRepairCost());

        return metrics;
    }

    public List<OverdueItem> getOverdueReport() {
        LocalDate today = LocalDate.now();
        List<IssueTransaction> overdueIssues = issueRepository.findOverdueIssues(today, AssetStatus.ISSUED);
        List<OverdueItem> results = new ArrayList<>();

        for (IssueTransaction issue : overdueIssues) {
            OverdueItem item = new OverdueItem();
            item.setIssueId(issue.getIssueId());
            item.setAssetId(issue.getAsset().getAssetId());
            item.setAssetCode(issue.getAsset().getAssetCode());
            item.setPackagingTypeName(issue.getAsset().getPackagingType().getTypeName());
            item.setCustomerName(issue.getCustomer().getCompanyName());
            item.setCustomerPhone(issue.getCustomer().getPhone());
            item.setIssueDate(issue.getIssueDate());
            item.setExpectedReturnDate(issue.getExpectedReturnDate());
            item.setDaysOverdue(ChronoUnit.DAYS.between(issue.getExpectedReturnDate(), today));
            results.add(item);
        }

        return results;
    }

    public List<CustomerOutstandingItem> getCustomerSummaryReport() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerOutstandingItem> summaries = new ArrayList<>();

        for (Customer c : customers) {
            CustomerOutstandingItem item = new CustomerOutstandingItem();
            item.setCustomerId(c.getCustomerId());
            item.setCompanyName(c.getCompanyName());
            item.setContactPerson(c.getContactPerson());
            item.setPhone(c.getPhone());

            long issuedCount = issueRepository.findByCustomerCustomerIdOrderByIssueDateDesc(c.getCustomerId()).size();
            long returnedCount = returnRepository.countByCustomerCustomerId(c.getCustomerId());
            long outstanding = Math.max(0, issuedCount - returnedCount);

            item.setTotalIssued(issuedCount);
            item.setTotalReturned(returnedCount);
            item.setCurrentlyOutstanding(outstanding);

            summaries.add(item);
        }

        return summaries;
    }
}
