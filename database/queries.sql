-- =============================================================================
-- SMART RETURNABLE PACKAGING TRACKING AND LIFECYCLE MANAGEMENT SYSTEM (SRPT-LMS)
-- Demonstration & Viva SQL Queries: queries.sql
-- =============================================================================

USE packaging_tracking;

-- =============================================================================
-- 1. BASIC FILTERING & SORTING (WHERE, ORDER BY, LIMIT)
-- Find all AVAILABLE assets of high-capacity metal containers
-- =============================================================================
SELECT 
    a.asset_id,
    a.asset_code,
    pt.type_name,
    pt.capacity_kg,
    w.warehouse_name,
    a.status
FROM assets a
JOIN packaging_types pt ON a.type_id = pt.type_id
JOIN warehouses w ON a.warehouse_id = w.warehouse_id
WHERE a.status = 'AVAILABLE' AND pt.capacity_kg >= 500.00
ORDER BY pt.capacity_kg DESC
LIMIT 10;

-- =============================================================================
-- 2. MULTI-TABLE INNER JOIN
-- Retrieve full operational details of all currently ISSUED assets
-- =============================================================================
SELECT 
    it.issue_id,
    a.asset_code,
    pt.type_name AS packaging_category,
    c.company_name AS customer_name,
    c.contact_person,
    c.phone,
    u.name AS issued_by_officer,
    it.issue_date,
    it.expected_return_date,
    it.purpose
FROM issue_transactions it
INNER JOIN assets a ON it.asset_id = a.asset_id
INNER JOIN packaging_types pt ON a.type_id = pt.type_id
INNER JOIN customers c ON it.customer_id = c.customer_id
INNER JOIN users u ON it.issued_by = u.user_id
WHERE a.status = 'ISSUED'
ORDER BY it.expected_return_date ASC;

-- =============================================================================
-- 3. LEFT OUTER JOIN (Showing all customers including those with 0 current issues)
-- =============================================================================
SELECT 
    c.customer_id,
    c.company_name,
    c.contact_person,
    COUNT(it.issue_id) AS total_times_issued,
    COUNT(rt.return_id) AS total_times_returned
FROM customers c
LEFT JOIN issue_transactions it ON c.customer_id = it.customer_id
LEFT JOIN return_transactions rt ON c.customer_id = rt.customer_id
GROUP BY c.customer_id, c.company_name, c.contact_person
ORDER BY total_times_issued DESC;

-- =============================================================================
-- 4. AGGREGATION WITH GROUP BY AND HAVING
-- Find packaging types that have more than 3 total registered assets
-- =============================================================================
SELECT 
    pt.type_id,
    pt.type_name,
    pt.capacity_kg,
    COUNT(a.asset_id) AS total_asset_count,
    SUM(CASE WHEN a.status = 'AVAILABLE' THEN 1 ELSE 0 END) AS ready_for_use
FROM packaging_types pt
INNER JOIN assets a ON pt.type_id = a.type_id
GROUP BY pt.type_id, pt.type_name, pt.capacity_kg
HAVING COUNT(a.asset_id) >= 3
ORDER BY total_asset_count DESC;

-- =============================================================================
-- 5. SUBQUERY: Find assets with repair cost higher than the system average repair cost
-- =============================================================================
SELECT 
    rr.repair_id,
    a.asset_code,
    pt.type_name,
    rr.repair_cost,
    rr.repair_status,
    rr.remarks
FROM repair_records rr
JOIN assets a ON rr.asset_id = a.asset_id
JOIN packaging_types pt ON a.type_id = pt.type_id
WHERE rr.repair_cost > (
    SELECT AVG(repair_cost) 
    FROM repair_records 
    WHERE repair_cost > 0
);

-- =============================================================================
-- 6. CORRELATED SUBQUERY: Get the latest movement location for every asset
-- =============================================================================
SELECT 
    a.asset_code,
    a.status,
    am.from_location,
    am.to_location,
    am.movement_date,
    u.name AS moved_by
FROM assets a
JOIN asset_movements am ON a.asset_id = am.asset_id
JOIN users u ON am.moved_by = u.user_id
WHERE am.movement_id = (
    SELECT MAX(sub_am.movement_id)
    FROM asset_movements sub_am
    WHERE sub_am.asset_id = a.asset_id
);

-- =============================================================================
-- 7. 360-DEGREE AUDIT TRAIL / LIFECYCLE TIMELINE FOR A SINGLE ASSET (e.g., MC-PUN-001)
-- =============================================================================
SELECT 
    '1. REGISTRATION' AS event_stage,
    a.created_at AS event_timestamp,
    CONCAT('Registered in Warehouse: ', w.warehouse_name, ' (Code: ', a.asset_code, ')') AS event_description
FROM assets a
JOIN warehouses w ON a.warehouse_id = w.warehouse_id
WHERE a.asset_id = 1

UNION ALL

SELECT 
    '2. ISSUED' AS event_stage,
    it.issue_date AS event_timestamp,
    CONCAT('Issued to ', c.company_name, ' for purpose: ', it.purpose, ' (Due: ', it.expected_return_date, ')')
FROM issue_transactions it
JOIN customers c ON it.customer_id = c.customer_id
WHERE it.asset_id = 1

UNION ALL

SELECT 
    '3. RETURNED' AS event_stage,
    rt.return_date AS event_timestamp,
    CONCAT('Returned by customer. Condition: ', rt.condition_status, ' - Remarks: ', COALESCE(rt.remarks, 'N/A'))
FROM return_transactions rt
WHERE rt.asset_id = 1

UNION ALL

SELECT 
    '4. REPAIRED' AS event_stage,
    COALESCE(rr.repair_date, rr.created_at) AS event_timestamp,
    CONCAT('Maintenance action: ', rr.repair_status, ' (Cost: INR ', rr.repair_cost, ') - ', rr.remarks)
FROM repair_records rr
WHERE rr.asset_id = 1

UNION ALL

SELECT 
    '5. MOVEMENT' AS event_stage,
    am.movement_date AS event_timestamp,
    CONCAT('Transferred from "', am.from_location, '" to "', am.to_location, '" - ', am.remarks)
FROM asset_movements am
WHERE am.asset_id = 1

ORDER BY event_timestamp ASC;
