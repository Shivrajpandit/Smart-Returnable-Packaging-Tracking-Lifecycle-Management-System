# Data Flow Diagrams (DFD): SRPT-LMS

## 1. Context Diagram (DFD Level 0)

The Level 0 DFD illustrates the boundary of the system, external entities, and major high-level data streams.

```mermaid
graph TD
    Admin[("👤 System Admin")]
    Staff[("👷 Warehouse Staff")]
    Manager[("👔 Manager / Executive")]

    System[["⚙️ SRPT-LMS Core System"]]

    Admin -->|"User, Master Catalog & Facility Data"| System
    System -->|"User Audit & System Status"| Admin

    Staff -->|"Issue, Inward Return, Damage & Repair Logs"| System
    System -->|"Asset Status & Operational Confirmations"| Staff

    Manager -->|"Report Requests & Filter Criteria"| System
    System -->|"KPI Metrics, Overdue & Repair Cost Reports"| Manager
```

---

## 2. Detailed Functional Diagram (DFD Level 1)

The Level 1 DFD decomposes the system into its core functional sub-processes and shows interactions with the 10 data stores.

```mermaid
graph TD
    %% Entities
    Admin[("👤 Admin")]
    Staff[("👷 Staff")]
    Manager[("👔 Manager")]

    %% Processes
    P1["1.0 Authentication & RBAC"]
    P2["2.0 Master Data Management"]
    P3["3.0 Asset Commissioning"]
    P4["4.0 Issue & Dispatch Engine"]
    P5["5.0 Return & Inspection Engine"]
    P6["6.0 Damage & Repair Lifecycle"]
    P7["7.0 Analytics & Overdue Reports"]

    %% Data Stores
    D1[("D1: users")]
    D2[("D2: customers")]
    D3[("D3: packaging_types")]
    D4[("D4: warehouses")]
    D5[("D5: assets")]
    D6[("D6: issue_transactions")]
    D7[("D7: return_transactions")]
    D8[("D8: damage_records")]
    D9[("D9: repair_records")]
    D10[("D10: asset_movements")]

    %% Flows
    Admin -->|"Credentials"| P1
    Staff -->|"Credentials"| P1
    Manager -->|"Credentials"| P1
    P1 <-->|"Verify & Hash"| D1

    Admin -->|"Manage Masters"| P2
    P2 <-->|"CRUD"| D2
    P2 <-->|"CRUD"| D3
    P2 <-->|"CRUD"| D4

    Staff -->|"Register Asset"| P3
    P3 -->|"Save Asset (AVAILABLE)"| D5

    Staff -->|"Checkout Asset"| P4
    P4 <-->|"Check AVAILABLE & Set ISSUED"| D5
    P4 -->|"Log Issue"| D6
    P4 -->|"Auto-log Movement"| D10

    Staff -->|"Inspect Return"| P5
    P5 <-->|"Set AVAILABLE / DAMAGED / RETIRED"| D5
    P5 -->|"Log Return"| D7
    P5 -->|"Auto-log Inward Transit"| D10

    Staff -->|"Record Damage & Repairs"| P6
    P6 <-->|"Set UNDER_REPAIR / AVAILABLE"| D5
    P6 -->|"Log Damage"| D8
    P6 -->|"Log Repair & Cost"| D9

    Manager -->|"Query Reports"| P7
    P7 <-->|"Read Status"| D5
    P7 <-->|"Calculate Overdue"| D6
    P7 <-->|"Sum Repair Costs"| D9
    P7 -->|"Deliver Dashboards"| Manager
```

---

## 3. End-to-End Operational Data Journey (From UI to DB & Back)

The following flowchart maps the exact path of data through the 3-tier technology stack for core transactional workflows:

```mermaid
flowchart TD
    subgraph UI_Tier ["🌐 Tier 1: Frontend Client (UI Events & Forms)"]
        UI_Issue["Staff Submits 'Issue Asset'\n(asset_id, customer_id, warehouse_id, expected_return_date)"]
        UI_Return["Staff Submits 'Return Asset'\n(issue_id, return_date, condition, notes)"]
        UI_Repair["Staff Submits 'Repair Asset'\n(damage_id, repair_cost, workshop, notes)"]
        UI_Report["Manager Requests 'KPI & Overdue'\n(date_range, warehouse_filter)"]
    end

    subgraph API_Tier ["⚙️ Tier 2A: REST API Controllers"]
        API_Issue["POST /api/issues"]
        API_Return["POST /api/returns"]
        API_Repair["POST /api/repairs"]
        API_Report["GET /api/reports/overdue & /api/reports/kpis"]
    end

    subgraph Service_Tier ["🧠 Tier 2B: Service & State Machine Layer"]
        S_Issue["IssueService:\n1. Validate status == 'AVAILABLE'\n2. Change status to 'ISSUED'\n3. Generate Issue Transaction\n4. Create Outward Movement record"]
        S_Return["ReturnService:\n1. Validate issue is open\n2. Log Return Transaction\n3. Create Inward Movement record\n4. Evaluate condition:\n   - GOOD: status='AVAILABLE'\n   - DAMAGED: status='DAMAGED' + auto-create Damage Record\n   - UNUSABLE: status='RETIRED'"]
        S_Repair["RepairService:\n1. Log Workshop repair ticket\n2. Transition status to 'UNDER_REPAIR'\n3. On completion, update actual cost & set status='AVAILABLE'"]
        S_Report["ReportService:\n1. Query active issues\n2. Compute datediff(NOW(), expected_date)\n3. Calculate total fleet utilization & repair ROI"]
    end

    subgraph DB_Tier ["🗄️ Tier 3: MySQL 8.0 3NF Relational Tables"]
        DB_Assets[("assets\n(status, current_warehouse_id)")]
        DB_Issues[("issue_transactions\n(issue_date, expected_return_date)")]
        DB_Returns[("return_transactions\n(return_date, condition_assessment)")]
        DB_Damages[("damage_records\n(severity, estimated_cost)")]
        DB_Repairs[("repair_records\n(repair_cost, workshop_details)")]
        DB_Movements[("asset_movements\n(origin_type, dest_type, movement_date)")]
    end

    %% Wiring Flows
    UI_Issue -->|"JSON payload"| API_Issue --> S_Issue
    S_Issue -->|"UPDATE status='ISSUED'"| DB_Assets
    S_Issue -->|"INSERT issue row"| DB_Issues
    S_Issue -->|"INSERT movement (Origin ➔ Customer)"| DB_Movements

    UI_Return -->|"JSON payload"| API_Return --> S_Return
    S_Return -->|"UPDATE status (AVAILABLE/DAMAGED/RETIRED)"| DB_Assets
    S_Return -->|"INSERT return row"| DB_Returns
    S_Return -->|"INSERT movement (Customer ➔ Warehouse)"| DB_Movements
    S_Return -.->|"IF DAMAGED: INSERT damage row"| DB_Damages

    UI_Repair -->|"JSON payload"| API_Repair --> S_Repair
    S_Repair -->|"UPDATE damage record"| DB_Damages
    S_Repair -->|"INSERT repair record"| DB_Repairs
    S_Repair -->|"UPDATE status='AVAILABLE'"| DB_Assets

    UI_Report -->|"HTTP query params"| API_Report --> S_Report
    S_Report <-->|"SELECT & JOIN calculations"| DB_Assets & DB_Issues & DB_Repairs

    %% Response cycle
    DB_Tier -.->|"Entities"| Service_Tier -.->|"DTOs"| API_Tier -.->|"JSON"| UI_Tier
```

---

## 4. Comprehensive Data Movement Mapping Matrix

| Source Process / Origin | Action / Trigger | Data Elements In Flight | Destination / Storage | Output Produced |
|---|---|---|---|---|
| **Admin Portal** | Create Packaging Master | Type Name, Material, Dimensions, Max Capacity (kg), Tare Weight | `packaging_types` table | New packaging profile active for asset assignment |
| **Admin Portal** | Register Warehouse | Facility Name, Code, City, Address, Contact | `warehouses` table | New regional hub ready for inventory stock |
| **Warehouse Portal** | Register Asset Unit | Asset Code, Packaging Type ID, Warehouse ID, Purchase Date, Cost | `assets` table (`status='AVAILABLE'`) | Barcoded asset unit added to fleet registry |
| **Checkout Portal** | Issue Outward Packaging | Asset ID, Customer ID, Warehouse ID, Expected Return Date | `issue_transactions`, `assets` (`status='ISSUED'`), `asset_movements` | Dispatch gate pass generated & asset marked in-transit/issued |
| **Inward Gate** | Return & Inspection | Issue ID, Return Date, Condition (`GOOD`/`DAMAGED`/`UNUSABLE`), Notes | `return_transactions`, `assets`, `asset_movements`, `damage_records` (conditional) | Return receipt; asset state routed to available, repair, or scrap |
| **Workshop Gate** | Maintenance & Repair | Damage ID, Workshop Details, Repair Action, Actual Cost | `repair_records`, `damage_records`, `assets` (`status='AVAILABLE'`) | Maintenance log closed; asset restored to active duty |
| **Audit Portal** | 360° Asset History Inquiry | Asset ID / Tag | Consolidated query across `assets`, `issues`, `returns`, `damages`, `repairs`, `movements` | Complete chronological lifetime audit timeline |
| **Executive Dashboard** | Analytics Request | Filter parameters (Date, Hub, Customer) | Dynamic SQL aggregation over `assets`, `issue_transactions`, `repair_records` | Real-time KPIs, overdue lists, repair expenses |

