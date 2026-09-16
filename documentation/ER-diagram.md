# Entity-Relationship (ER) Diagram: SRPT-LMS

The following Entity-Relationship Diagram models all 10 core tables in **3rd Normal Form (3NF)** with exact primary keys, foreign keys, and cardinalities.

```mermaid
erDiagram
    users ||--o{ issue_transactions : "issues"
    users ||--o{ return_transactions : "receives"
    users ||--o{ damage_records : "reports"
    users ||--o{ repair_records : "repairs"
    users ||--o{ asset_movements : "moves"

    customers ||--o{ issue_transactions : "receives"
    customers ||--o{ return_transactions : "returns"

    packaging_types ||--o{ assets : "categorizes"
    warehouses ||--o{ assets : "houses"

    assets ||--o{ issue_transactions : "undergoes"
    assets ||--o{ return_transactions : "undergoes"
    assets ||--o{ damage_records : "incurs"
    assets ||--o{ repair_records : "undergoes"
    assets ||--o{ asset_movements : "undergoes"

    damage_records ||--o| repair_records : "triggers"

    users {
        int user_id PK
        string name
        string email UK
        string password
        enum role
        timestamp created_at
    }

    customers {
        int customer_id PK
        string company_name
        string contact_person
        string phone
        string email
        string address
        timestamp created_at
    }

    packaging_types {
        int type_id PK
        string type_name UK
        string description
        decimal capacity_kg
    }

    warehouses {
        int warehouse_id PK
        string warehouse_name
        string location
        string manager_name
        string contact_number
    }

    assets {
        int asset_id PK
        int type_id FK
        int warehouse_id FK
        string asset_code UK
        date purchase_date
        enum status
        timestamp created_at
    }

    issue_transactions {
        int issue_id PK
        int asset_id FK
        int customer_id FK
        int issued_by FK
        date issue_date
        date expected_return_date
        string purpose
        timestamp created_at
    }

    return_transactions {
        int return_id PK
        int asset_id FK
        int customer_id FK
        int received_by FK
        date return_date
        enum condition_status
        string remarks
        timestamp created_at
    }

    damage_records {
        int damage_id PK
        int asset_id FK
        int reported_by FK
        string damage_type
        enum severity
        date damage_date
        decimal estimated_cost
        text description
        timestamp created_at
    }

    repair_records {
        int repair_id PK
        int asset_id FK
        int damage_id FK
        int repaired_by FK
        date repair_date
        decimal repair_cost
        enum repair_status
        string remarks
        timestamp created_at
    }

    asset_movements {
        int movement_id PK
        int asset_id FK
        string from_location
        string to_location
        datetime movement_date
        int moved_by FK
        string remarks
    }
```

---

## Relationship Cardinalities Summary

1. **`packaging_types` to `assets` (1 : M)**: One packaging type classifies multiple asset instances. Each asset belongs to exactly one packaging category.
2. **`warehouses` to `assets` (1 : M)**: One warehouse hub manages multiple assets. Each asset has one assigned home base.
3. **`customers` to `issue_transactions` (1 : M)**: One customer can receive multiple packaging shipments over time.
4. **`assets` to `issue_transactions` (1 : M)**: One reusable packaging unit undergoes multiple checkout issues throughout its service lifespan.
5. **`assets` to `return_transactions` (1 : M)**: One asset accumulates a log of historical returns and condition reports.
6. **`damage_records` to `repair_records` (1 : 1 or 1 : 0)**: A damage incident report can optionally trigger a workshop repair order.
