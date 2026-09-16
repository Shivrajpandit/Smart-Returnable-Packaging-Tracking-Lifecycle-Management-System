package com.packaging.controller;

import com.packaging.dto.ReportDTO.*;
import com.packaging.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardMetrics> getDashboardMetrics() {
        return ResponseEntity.ok(reportService.getDashboardMetrics());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<OverdueItem>> getOverdueReport() {
        return ResponseEntity.ok(reportService.getOverdueReport());
    }

    @GetMapping("/customer-summary")
    public ResponseEntity<List<CustomerOutstandingItem>> getCustomerSummary() {
        return ResponseEntity.ok(reportService.getCustomerSummaryReport());
    }
}
