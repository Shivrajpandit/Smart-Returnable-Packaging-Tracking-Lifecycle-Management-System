-- =============================================================================
-- SMART RETURNABLE PACKAGING TRACKING AND LIFECYCLE MANAGEMENT SYSTEM (SRPT-LMS)
-- Sample Data Insertion Script: sample_data.sql
-- =============================================================================

USE packaging_tracking;

-- Clean existing data in reverse foreign key order
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE asset_movements;
TRUNCATE TABLE repair_records;
TRUNCATE TABLE damage_records;
TRUNCATE TABLE return_transactions;
TRUNCATE TABLE issue_transactions;
TRUNCATE TABLE assets;
TRUNCATE TABLE warehouses;
TRUNCATE TABLE packaging_types;
TRUNCATE TABLE customers;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- 1. Insert Users
-- Passwords below are BCrypt hashed for:
-- 1. admin@packaging.com -> admin123
-- 2. staff@packaging.com -> staff123
-- 3. manager@packaging.com -> manager123
-- Hash: $2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3ee
-- =============================================================================
INSERT INTO users (user_id, name, email, password, role) VALUES
(1, 'Rajesh Sharma', 'admin@packaging.com', '$2a$10$WxbaAPyL77tgE3Vs1Yibc./HpGPCzfpkga2nwOiF7BcD4FevhoP0q', 'ADMIN'),
(2, 'Amit Verma', 'staff@packaging.com', '$2a$10$WxbaAPyL77tgE3Vs1Yibc./HpGPCzfpkga2nwOiF7BcD4FevhoP0q', 'WAREHOUSE_STAFF'),
(3, 'Pooja Nair', 'manager@packaging.com', '$2a$10$WxbaAPyL77tgE3Vs1Yibc./HpGPCzfpkga2nwOiF7BcD4FevhoP0q', 'MANAGER');

-- =============================================================================
-- 2. Insert Customers (Realistic Indian Manufacturing & Logistics Firms)
-- =============================================================================
INSERT INTO customers (customer_id, company_name, contact_person, phone, email, address) VALUES
(1, 'Tata AutoComp Systems Ltd', 'Suresh Deshmukh', '+91 98220 11223', 'suresh.d@tataautocomp.example.com', 'Plot 25, MIDC Bhosari, Pune, MH - 411026'),
(2, 'Mahindra Logistics Solutions', 'Kavita Menon', '+91 98401 44556', 'kavita.m@mahindralog.example.com', 'Mahindra Towers, Worli, Mumbai, MH - 400018'),
(3, 'Bharat Forge Components', 'Anand Kulkarni', '+91 98902 77889', 'anand.k@bharatforge.example.com', 'Mundhwa Industrial Area, Pune, MH - 411036'),
(4, 'Bajaj Auto Spares Division', 'Ravi Iyer', '+91 97654 33221', 'ravi.iyer@bajajspares.example.com', 'Akurdi Industrial Hub, Pune, MH - 411035'),
(5, 'Reliance Retail Supply Chain', 'Sunil Bansal', '+91 98111 88990', 'sunil.b@relsupply.example.com', 'Sector 34, Industrial Complex, Gurugram, HR - 122004');

-- =============================================================================
-- 3. Insert Packaging Types
-- =============================================================================
INSERT INTO packaging_types (type_id, type_name, description, capacity_kg) VALUES
(1, 'Heavy-Duty Collapsible Metal Container', 'Galvanized steel stackable mesh container with drop-gate', 1200.00),
(2, 'Industrial HDPE Plastic Crate (600x400)', 'High-density polyethylene returnable crate for precision components', 45.00),
(3, 'Euro Standard Wooden/Composite Pallet', 'Standard heat-treated 4-way entry logistics pallet (1200x800mm)', 1500.00),
(4, '200L Steel Chemical & Liquid Drum', 'Epoxy-lined UN certified heavy gauge steel closed-head drum', 250.00),
(5, 'Thermal Insulated Transport Box (Cooler)', 'Double-walled temperature-controlled EPP box with dry-ice slots', 35.00);

-- =============================================================================
-- 4. Insert Warehouses
-- =============================================================================
INSERT INTO warehouses (warehouse_id, warehouse_name, location, manager_name, contact_number) VALUES
(1, 'Pune Central Logistics Hub', 'Chakan MIDC Phase 2, Pune, Maharashtra', 'Pooja Nair', '+91 20 6711 9001'),
(2, 'Chennai Automotive Distribution Center', 'Sriperumbudur Industrial Corridor, Chennai, Tamil Nadu', 'Murugan Swamy', '+91 44 4822 5500'),
(3, 'NCR North Regional Warehouse', 'IMT Manesar Sector 8, Gurugram, Haryana', 'Deepak Chauhan', '+91 124 4567 890');

-- =============================================================================
-- 5. Insert 22 Physical Assets (Covering all lifecycle states)
-- =============================================================================
INSERT INTO assets (asset_id, type_id, warehouse_id, asset_code, purchase_date, status) VALUES
-- Pune Central (Warehouse 1)
(1, 1, 1, 'MC-PUN-001', '2024-01-15', 'AVAILABLE'),
(2, 1, 1, 'MC-PUN-002', '2024-01-15', 'ISSUED'),
(3, 1, 1, 'MC-PUN-003', '2024-02-10', 'ISSUED'),
(4, 2, 1, 'PC-PUN-101', '2024-03-01', 'AVAILABLE'),
(5, 2, 1, 'PC-PUN-102', '2024-03-01', 'AVAILABLE'),
(6, 2, 1, 'PC-PUN-103', '2024-03-01', 'DAMAGED'),
(7, 3, 1, 'PL-PUN-201', '2023-11-20', 'UNDER_REPAIR'),
(8, 3, 1, 'PL-PUN-202', '2023-11-20', 'AVAILABLE'),
(9, 4, 1, 'DR-PUN-301', '2024-04-05', 'ISSUED'),
(10, 5, 1, 'TB-PUN-401', '2024-05-12', 'AVAILABLE'),

-- Chennai Distribution Center (Warehouse 2)
(11, 1, 2, 'MC-CHN-001', '2024-01-20', 'AVAILABLE'),
(12, 1, 2, 'MC-CHN-002', '2024-01-20', 'ISSUED'),
(13, 2, 2, 'PC-CHN-101', '2024-02-15', 'IN_TRANSIT'),
(14, 2, 2, 'PC-CHN-102', '2024-02-15', 'AVAILABLE'),
(15, 3, 2, 'PL-CHN-201', '2023-10-10', 'RETIRED'),
(16, 4, 2, 'DR-CHN-301', '2024-03-18', 'AVAILABLE'),
(17, 5, 2, 'TB-CHN-401', '2024-05-20', 'LOST'),

-- NCR North Regional Warehouse (Warehouse 3)
(18, 1, 3, 'MC-NCR-001', '2024-02-01', 'AVAILABLE'),
(19, 2, 3, 'PC-NCR-101', '2024-02-10', 'ISSUED'),
(20, 3, 3, 'PL-NCR-201', '2023-12-05', 'AVAILABLE'),
(21, 4, 3, 'DR-NCR-301', '2024-04-01', 'UNDER_REPAIR'),
(22, 5, 3, 'TB-NCR-401', '2024-05-01', 'AVAILABLE');

-- =============================================================================
-- 6. Insert Issue Transactions (Both Historical & Active/Overdue)
-- =============================================================================
INSERT INTO issue_transactions (issue_id, asset_id, customer_id, issued_by, issue_date, expected_return_date, purpose) VALUES
-- Historical Returned Issues
(1, 1, 1, 2, '2024-05-01', '2024-05-15', 'Chassis sub-assembly transit batch #401'),
(2, 4, 3, 2, '2024-05-10', '2024-05-25', 'Gearbox precision fasteners packaging'),
(3, 7, 2, 2, '2024-05-12', '2024-05-26', 'Palletized bulk shipment to dealership hub'),

-- Active Issued Assets (Regular)
(4, 2, 1, 2, '2026-09-10', '2026-09-24', 'Export batch aluminum casting transport'),
(5, 9, 4, 2, '2026-09-12', '2026-09-26', 'Engine synthetic lubricant bulk circulation'),
(6, 12, 1, 2, '2026-09-14', '2026-09-28', 'Transmission parts interstate supply'),

-- Active Issued Assets (OVERDUE - expected return was in the past!)
(7, 3, 3, 2, '2026-08-01', '2026-08-15', 'Crankshaft batch delivery to casting unit'),
(8, 19, 5, 2, '2026-07-20', '2026-08-05', 'Perishable food packaging seasonal dispatch');

-- =============================================================================
-- 7. Insert Return Transactions
-- =============================================================================
INSERT INTO return_transactions (return_id, asset_id, customer_id, received_by, return_date, condition_status, remarks) VALUES
-- Asset 1 returned in Good Condition
(1, 1, 1, 2, '2024-05-14', 'GOOD', 'Received clean with all locking pins intact'),

-- Asset 4 returned in Good Condition
(2, 4, 3, 2, '2024-05-24', 'GOOD', 'Standard wear, ready for reuse'),

-- Asset 7 returned with Minor Damage (Pallet cracked board)
(3, 7, 2, 2, '2024-05-27', 'MINOR_DAMAGE', 'Base runner cracked during forklift unloading'),

-- Asset 6 returned with Major Damage previously
(4, 6, 4, 2, '2024-06-02', 'MAJOR_DAMAGE', 'Wall structural bend, side latch fractured'),

-- Asset 15 returned Unusable
(5, 15, 2, 2, '2024-04-10', 'UNUSABLE', 'Severe structural collapse beyond economical repair');

-- =============================================================================
-- 8. Insert Damage Records
-- =============================================================================
INSERT INTO damage_records (damage_id, asset_id, reported_by, damage_type, severity, damage_date, estimated_cost, description) VALUES
(1, 7, 2, 'Base Board Fracture', 'MEDIUM', '2024-05-27', 450.00, 'Heavy forklift pressure cracked middle cross-plank'),
(2, 6, 2, 'Corner Wall Deformation', 'HIGH', '2024-06-02', 1200.00, 'Impact during loading dock handling bent HDPE side wall'),
(3, 21, 2, 'Flange & Rim Dent', 'MEDIUM', '2024-06-15', 750.00, 'Top sealing rim dented causing airtight seal breach');

-- =============================================================================
-- 9. Insert Repair Records
-- =============================================================================
INSERT INTO repair_records (repair_id, asset_id, damage_id, repaired_by, repair_date, repair_cost, repair_status, remarks) VALUES
-- Repaired asset 7 (Currently in progress / finishing)
(1, 7, 1, 2, NULL, 450.00, 'IN_PROGRESS', 'Replacement hardwood cross-plank undergoing bolting'),

-- Repair on Asset 21 (Pending inspection)
(2, 21, 3, 2, NULL, 750.00, 'PENDING', 'Awaiting hydraulic rim re-alignment tool'),

-- Historical Completed Repair (Asset 1 in the past)
(3, 1, NULL, 2, '2024-02-18', 350.00, 'COMPLETED', 'Locking latch lubricated and weld reinforced');

-- =============================================================================
-- 10. Insert Asset Movements
-- =============================================================================
INSERT INTO asset_movements (movement_id, asset_id, from_location, to_location, movement_date, moved_by, remarks) VALUES
(1, 1, 'Vendor Facility (Pune)', 'Pune Central Logistics Hub', '2024-01-15 10:30:00', 1, 'Initial receiving into central warehouse inventory'),
(2, 1, 'Pune Central Logistics Hub', 'Tata AutoComp Systems Ltd', '2024-05-01 14:00:00', 2, 'Outward shipment via truck MH-12-RN-8800'),
(3, 1, 'Tata AutoComp Systems Ltd', 'Pune Central Logistics Hub', '2024-05-14 11:20:00', 2, 'Inward gate return inspection cleared'),
(4, 13, 'Chennai Distribution Center', 'Pune Central Logistics Hub', '2026-09-15 08:00:00', 2, 'Inter-warehouse inventory balancing transit'),
(5, 7, 'Pune Central Bay 4', 'Maintenance Workshop Dock B', '2024-05-28 09:15:00', 2, 'Transferred to carpentry repair section');
