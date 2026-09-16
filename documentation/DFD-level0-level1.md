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
