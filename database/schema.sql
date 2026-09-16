-- =============================================================================
-- SMART RETURNABLE PACKAGING TRACKING AND LIFECYCLE MANAGEMENT SYSTEM (SRPT-LMS)
-- Database DDL Script: schema.sql
-- =============================================================================

-- Step 1: Create Database
DROP DATABASE IF EXISTS packaging_tracking;
CREATE DATABASE packaging_tracking
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE packaging_tracking;

-- =============================================================================
-- 1. Table: users
-- Purpose: System authentication and role-based authorization
-- =============================================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'WAREHOUSE_STAFF', 'MANAGER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================================================================
-- 2. Table: customers
-- Purpose: Client organizations that receive and return packaging assets
-- =============================================================================
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(150) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================================================================
-- 3. Table: packaging_types
-- Purpose: Master catalog of packaging types (crates, pallets, drums, etc.)
-- =============================================================================
CREATE TABLE packaging_types (
    type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    capacity_kg DECIMAL(10,2) NOT NULL CHECK (capacity_kg > 0)
) ENGINE=InnoDB;

-- =============================================================================
-- 4. Table: warehouses
-- Purpose: Physical storage facilities managing packaging inventory
-- =============================================================================
CREATE TABLE warehouses (
    warehouse_id INT PRIMARY KEY AUTO_INCREMENT,
    warehouse_name VARCHAR(150) NOT NULL,
    location VARCHAR(255) NOT NULL,
    manager_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- =============================================================================
-- 5. Table: assets
-- Purpose: Core entity tracking individual physical returnable assets
-- =============================================================================
CREATE TABLE assets (
    asset_id INT PRIMARY KEY AUTO_INCREMENT,
    type_id INT NOT NULL,
    warehouse_id INT NOT NULL,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    purchase_date DATE NOT NULL,
    status ENUM(
        'AVAILABLE',
        'ISSUED',
        'IN_TRANSIT',
        'DAMAGED',
        'UNDER_REPAIR',
        'LOST',
        'RETIRED'
    ) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assets_type FOREIGN KEY (type_id) 
        REFERENCES packaging_types(type_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_assets_warehouse FOREIGN KEY (warehouse_id) 
        REFERENCES warehouses(warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- 6. Table: issue_transactions
-- Purpose: Log records of packaging assets issued to customers
-- =============================================================================
CREATE TABLE issue_transactions (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    asset_id INT NOT NULL,
    customer_id INT NOT NULL,
    issued_by INT NOT NULL,
    issue_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    purpose VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_issue_asset FOREIGN KEY (asset_id) 
        REFERENCES assets(asset_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_issue_customer FOREIGN KEY (customer_id) 
        REFERENCES customers(customer_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_issue_user FOREIGN KEY (issued_by) 
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_issue_dates CHECK (expected_return_date >= issue_date)
) ENGINE=InnoDB;

-- =============================================================================
-- 7. Table: return_transactions
-- Purpose: Log records of returned assets and condition assessment
-- =============================================================================
CREATE TABLE return_transactions (
    return_id INT PRIMARY KEY AUTO_INCREMENT,
    asset_id INT NOT NULL,
    customer_id INT NOT NULL,
    received_by INT NOT NULL,
    return_date DATE NOT NULL,
    condition_status ENUM('GOOD', 'MINOR_DAMAGE', 'MAJOR_DAMAGE', 'UNUSABLE') NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_return_asset FOREIGN KEY (asset_id) 
        REFERENCES assets(asset_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_return_customer FOREIGN KEY (customer_id) 
        REFERENCES customers(customer_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_return_user FOREIGN KEY (received_by) 
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- 8. Table: damage_records
-- Purpose: Incident reports for assets identified as damaged
-- =============================================================================
CREATE TABLE damage_records (
    damage_id INT PRIMARY KEY AUTO_INCREMENT,
    asset_id INT NOT NULL,
    reported_by INT NOT NULL,
    damage_type VARCHAR(100) NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    damage_date DATE NOT NULL,
    estimated_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (estimated_cost >= 0),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_damage_asset FOREIGN KEY (asset_id) 
        REFERENCES assets(asset_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_damage_user FOREIGN KEY (reported_by) 
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- 9. Table: repair_records
-- Purpose: Maintenance and repair tracking linked to damages
-- =============================================================================
CREATE TABLE repair_records (
    repair_id INT PRIMARY KEY AUTO_INCREMENT,
    asset_id INT NOT NULL,
    damage_id INT,
    repaired_by INT NOT NULL,
    repair_date DATE,
    repair_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (repair_cost >= 0),
    repair_status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_repair_asset FOREIGN KEY (asset_id) 
        REFERENCES assets(asset_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_repair_damage FOREIGN KEY (damage_id) 
        REFERENCES damage_records(damage_id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_repair_user FOREIGN KEY (repaired_by) 
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- 10. Table: asset_movements
-- Purpose: Facility-to-facility and internal logistics tracking
-- =============================================================================
CREATE TABLE asset_movements (
    movement_id INT PRIMARY KEY AUTO_INCREMENT,
    asset_id INT NOT NULL,
    from_location VARCHAR(150) NOT NULL,
    to_location VARCHAR(150) NOT NULL,
    movement_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    moved_by INT NOT NULL,
    remarks VARCHAR(255),
    CONSTRAINT fk_movement_asset FOREIGN KEY (asset_id) 
        REFERENCES assets(asset_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_movement_user FOREIGN KEY (moved_by) 
        REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- INDEXES FOR PERFORMANCE & FAST LOOKUPS
-- =============================================================================
CREATE INDEX idx_assets_status ON assets(status);
CREATE INDEX idx_assets_code ON assets(asset_code);
CREATE INDEX idx_issue_dates ON issue_transactions(issue_date, expected_return_date);
CREATE INDEX idx_issue_customer ON issue_transactions(customer_id);
CREATE INDEX idx_return_asset ON return_transactions(asset_id);
CREATE INDEX idx_damage_asset ON damage_records(asset_id);
CREATE INDEX idx_repair_status ON repair_records(repair_status);
CREATE INDEX idx_movement_asset ON asset_movements(asset_id);

-- =============================================================================
-- DATABASE VIEWS (DBMS CONCEPT DEMONSTRATION)
-- =============================================================================

-- View 1: Overall Warehouse Inventory & Status Breakdown
CREATE OR REPLACE VIEW view_inventory_summary AS
SELECT 
    pt.type_name,
    w.warehouse_name,
    COUNT(a.asset_id) AS total_assets,
    SUM(CASE WHEN a.status = 'AVAILABLE' THEN 1 ELSE 0 END) AS available_count,
    SUM(CASE WHEN a.status = 'ISSUED' THEN 1 ELSE 0 END) AS issued_count,
    SUM(CASE WHEN a.status = 'IN_TRANSIT' THEN 1 ELSE 0 END) AS in_transit_count,
    SUM(CASE WHEN a.status = 'DAMAGED' THEN 1 ELSE 0 END) AS damaged_count,
    SUM(CASE WHEN a.status = 'UNDER_REPAIR' THEN 1 ELSE 0 END) AS under_repair_count,
    SUM(CASE WHEN a.status = 'LOST' THEN 1 ELSE 0 END) AS lost_count,
    SUM(CASE WHEN a.status = 'RETIRED' THEN 1 ELSE 0 END) AS retired_count
FROM packaging_types pt
JOIN assets a ON pt.type_id = a.type_id
JOIN warehouses w ON a.warehouse_id = w.warehouse_id
GROUP BY pt.type_name, w.warehouse_name;

-- View 2: Overdue Issued Assets
CREATE OR REPLACE VIEW view_overdue_assets AS
SELECT 
    it.issue_id,
    a.asset_id,
    a.asset_code,
    pt.type_name,
    c.company_name AS customer_name,
    c.phone AS customer_phone,
    c.email AS customer_email,
    it.issue_date,
    it.expected_return_date,
    DATEDIFF(CURRENT_DATE, it.expected_return_date) AS days_overdue,
    u.name AS issued_by_staff
FROM issue_transactions it
JOIN assets a ON it.asset_id = a.asset_id
JOIN packaging_types pt ON a.type_id = pt.type_id
JOIN customers c ON it.customer_id = c.customer_id
JOIN users u ON it.issued_by = u.user_id
WHERE a.status = 'ISSUED' 
  AND it.expected_return_date < CURRENT_DATE
  AND it.issue_id = (
      -- Get latest issue transaction for this asset
      SELECT MAX(latest.issue_id) 
      FROM issue_transactions latest 
      WHERE latest.asset_id = a.asset_id
  );

-- View 3: Customer Outstanding Return Summary
CREATE OR REPLACE VIEW view_customer_outstanding AS
SELECT 
    c.customer_id,
    c.company_name,
    c.contact_person,
    c.phone,
    COUNT(DISTINCT it.issue_id) AS total_issued_times,
    COUNT(DISTINCT rt.return_id) AS total_returned_times,
    SUM(CASE WHEN a.status = 'ISSUED' THEN 1 ELSE 0 END) AS currently_holding_assets
FROM customers c
LEFT JOIN issue_transactions it ON c.customer_id = it.customer_id
LEFT JOIN return_transactions rt ON c.customer_id = rt.customer_id
LEFT JOIN assets a ON it.asset_id = a.asset_id
GROUP BY c.customer_id, c.company_name, c.contact_person, c.phone;
