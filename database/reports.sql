-- =============================================================================
-- SMART RETURNABLE PACKAGING TRACKING AND LIFECYCLE MANAGEMENT SYSTEM (SRPT-LMS)
-- Managerial Analytical Reports: reports.sql
-- =============================================================================

USE packaging_tracking;

-- =============================================================================
-- REPORT 1: INVENTORY & UTILIZATION KPI SUMMARY
-- =============================================================================
SELECT 
    w.warehouse_name,
    COUNT(a.asset_id) AS total_fleet_size,
    SUM(CASE WHEN a.status = 'AVAILABLE' THEN 1 ELSE 0 END) AS available_in_stock,
    SUM(CASE WHEN a.status = 'ISSUED' THEN 1 ELSE 0 END) AS currently_circulating,
    SUM(CASE WHEN a.status = 'DAMAGED' THEN 1 ELSE 0 END) AS damaged_awaiting_action,
    SUM(CASE WHEN a.status = 'UNDER_REPAIR' THEN 1 ELSE 0 END) AS under_active_repair,
    SUM(CASE WHEN a.status = 'LOST' THEN 1 ELSE 0 END) AS missing_lost,
    SUM(CASE WHEN a.status = 'RETIRED' THEN 1 ELSE 0 END) AS decommissioned,
    ROUND((SUM(CASE WHEN a.status = 'ISSUED' THEN 1 ELSE 0 END) / COUNT(a.asset_id)) * 100, 2) AS utilization_rate_percentage
FROM warehouses w
LEFT JOIN assets a ON w.warehouse_id = a.warehouse_id
GROUP BY w.warehouse_id, w.warehouse_name;

-- =============================================================================
-- REPORT 2: OVERDUE ASSET AGING REPORT
-- =============================================================================
SELECT 
    a.asset_code,
    pt.type_name,
    c.company_name AS customer_name,
    c.contact_person,
    c.phone,
    it.issue_date,
    it.expected_return_date,
    DATEDIFF(CURRENT_DATE, it.expected_return_date) AS overdue_days,
    CASE 
        WHEN DATEDIFF(CURRENT_DATE, it.expected_return_date) > 30 THEN 'CRITICAL (>30 Days)'
        WHEN DATEDIFF(CURRENT_DATE, it.expected_return_date) > 15 THEN 'HIGH (15-30 Days)'
        ELSE 'MODERATE (<15 Days)'
    END AS risk_tier
FROM issue_transactions it
JOIN assets a ON it.asset_id = a.asset_id
JOIN packaging_types pt ON a.type_id = pt.type_id
JOIN customers c ON it.customer_id = c.customer_id
WHERE a.status = 'ISSUED' 
  AND it.expected_return_date < CURRENT_DATE
ORDER BY overdue_days DESC;

-- =============================================================================
-- REPORT 3: CUSTOMER ASSET EXPOSURE & RETURN COMPLIANCE
-- =============================================================================
SELECT 
    c.customer_id,
    c.company_name,
    COUNT(DISTINCT it.issue_id) AS total_lifetime_issues,
    COUNT(DISTINCT rt.return_id) AS total_returned_batches,
    (COUNT(DISTINCT it.issue_id) - COUNT(DISTINCT rt.return_id)) AS net_unreturned_units,
    SUM(CASE WHEN a.status = 'ISSUED' AND it.expected_return_date < CURRENT_DATE THEN 1 ELSE 0 END) AS overdue_units
FROM customers c
LEFT JOIN issue_transactions it ON c.customer_id = it.customer_id
LEFT JOIN return_transactions rt ON c.customer_id = rt.customer_id
LEFT JOIN assets a ON it.asset_id = a.asset_id
GROUP BY c.customer_id, c.company_name
ORDER BY net_unreturned_units DESC;

-- =============================================================================
-- REPORT 4: DAMAGE INCIDENT & SEVERITY BREAKDOWN
-- =============================================================================
SELECT 
    pt.type_name,
    dr.severity,
    COUNT(dr.damage_id) AS incident_count,
    SUM(dr.estimated_cost) AS total_estimated_damage_cost,
    ROUND(AVG(dr.estimated_cost), 2) AS average_damage_cost
FROM damage_records dr
JOIN assets a ON dr.asset_id = a.asset_id
JOIN packaging_types pt ON a.type_id = pt.type_id
GROUP BY pt.type_name, dr.severity
ORDER BY total_estimated_damage_cost DESC;

-- =============================================================================
-- REPORT 5: TOTAL REPAIR COST EXPENDITURE ANALYSIS
-- =============================================================================
SELECT 
    pt.type_name,
    COUNT(rr.repair_id) AS total_repairs_count,
    SUM(CASE WHEN rr.repair_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_repairs,
    SUM(CASE WHEN rr.repair_status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS active_repairs,
    SUM(CASE WHEN rr.repair_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_repairs,
    SUM(rr.repair_cost) AS total_repair_expenditure_inr,
    ROUND(AVG(rr.repair_cost), 2) AS avg_cost_per_repair
FROM repair_records rr
JOIN assets a ON rr.asset_id = a.asset_id
JOIN packaging_types pt ON a.type_id = pt.type_id
GROUP BY pt.type_name
ORDER BY total_repair_expenditure_inr DESC;
