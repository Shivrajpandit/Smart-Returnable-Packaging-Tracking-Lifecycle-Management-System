# SRPT-LMS: 1-Page Quick Viva Cheat-Sheet 🎓

> **Print or keep this 1-page summary open for instant reference right before your viva!**

---

## 1. Project At A Glance

* **Project Name:** Smart Returnable Packaging Tracking and Lifecycle Management System (SRPT-LMS)
* **Goal:** Replace manual registers with a centralized 3NF DBMS to track returnable containers across manufacturing, warehouse, and customer hubs.
* **Tech Stack:**
  * **Frontend:** HTML5, CSS3, Bootstrap 5, Vanilla JavaScript ES6 (Fetch API).
  * **Backend:** Java 17, Spring Boot 3.2, Spring Data JPA / Hibernate, Spring Security Crypto.
  * **Database:** MySQL 8.0+ (`packaging_tracking`).

---

## 2. 10 Core Tables & Normalization (3NF)

* **Tables:** `users`, `customers`, `packaging_types`, `warehouses`, `assets`, `issue_transactions`, `return_transactions`, `damage_records`, `repair_records`, `asset_movements`.
* **1NF:** Atomic values, PK on all tables (`user_id`, `asset_id`, etc.), no repeating groups.
* **2NF:** 1NF + No partial dependencies on composite keys (all tables use single surrogate PKs).
* **3NF:** 2NF + **No transitive dependencies** ($A \rightarrow B \rightarrow C$). Customer and packaging specs are in separate master catalogs, not duplicated in transaction tables.

---

## 3. Asset Lifecycle State Transitions (Memorize This!)

$$\text{Registration} \xrightarrow{} \mathbf{AVAILABLE} \xrightarrow[\text{Asset}]{\text{Issue}} \mathbf{ISSUED} \xrightarrow[\text{Asset}]{\text{Return}} \text{Inspection} \begin{cases} \text{GOOD} \rightarrow \mathbf{AVAILABLE} \\ \text{DAMAGE} \rightarrow \mathbf{DAMAGED} \xrightarrow[\text{Repair}]{\text{Start}} \mathbf{UNDER\_REPAIR} \xrightarrow[\text{Done}]{\text{Repair}} \mathbf{AVAILABLE} \\ \text{UNUSABLE} \rightarrow \mathbf{RETIRED} \end{cases}$$

---

## 4. Key SQL Concepts & Queries

| Concept | Explanation | Example from Code |
| --- | --- | --- |
| **Multi-Table Join** | Joins 4 tables for issue details | `FROM issue_transactions it JOIN assets a ON it.asset_id=a.asset_id JOIN customers c ON it.customer_id=c.customer_id JOIN users u ON it.issued_by=u.user_id` |
| **Outer Join** | Shows all customers even with 0 issues | `FROM customers c LEFT JOIN issue_transactions it ON c.customer_id = it.customer_id` |
| **Group By & Having** | Filter categories with $\ge 3$ assets | `GROUP BY pt.type_id HAVING COUNT(a.asset_id) >= 3` |
| **Overdue Logic** | Identify unreturned assets past due | `WHERE a.status = 'ISSUED' AND it.expected_return_date < CURRENT_DATE` |
| **Aggregate Cost** | Total workshop maintenance | `SELECT SUM(repair_cost) FROM repair_records` |
| **Subquery** | Repairs above average cost | `WHERE repair_cost > (SELECT AVG(repair_cost) FROM repair_records)` |

---

## 5. Security & Architecture

* **Layered Architecture:** `Controller` (HTTP routes) $\rightarrow$ `Service` (Business rules & transactions) $\rightarrow$ `Repository` (JPA/Hibernate) $\rightarrow$ `Database` (MySQL).
* **Password Encryption:** Passwords hashed using **BCrypt** salt hashing algorithm (`BCryptPasswordEncoder`).
* **Roles (RBAC):**
  * `ADMIN`: Master catalogs, user accounts, system configuration.
  * `WAREHOUSE_STAFF`: Issue assets, receive returns, log damage, workshop repairs, movements.
  * `MANAGER`: Executive KPI dashboards, overdue return monitors, cost analytics.

---

## 6. Key REST Endpoints

* **Auth:** `POST /api/auth/login`, `POST /api/auth/register`
* **Assets:** `GET /api/assets?status=AVAILABLE`, `POST /api/assets`, `GET /api/assets/{id}/history` (360° timeline)
* **Lifecycle:** `POST /api/issues`, `POST /api/returns`, `POST /api/damages`, `POST /api/repairs`, `PUT /api/repairs/{id}`
* **Reports:** `GET /api/reports/dashboard`, `GET /api/reports/overdue`, `GET /api/reports/customer-summary`

---

## 7. Golden Viva Rapid-Fire Answers

* **Why use `@Transactional`?** Ensures Atomicity: all database operations (updating asset status + logging movement) succeed together or rollback completely if an error occurs.
* **Why use Database Views?** Simplifies complex joins into a reusable virtual table and protects underlying schema.
* **Why use Surrogate Keys?** Auto-increment integers (`asset_id`) provide immutable primary keys unaffected by business changes to `asset_code`.
