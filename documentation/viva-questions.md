# SRPT-LMS: 30+ Comprehensive DBMS & Full-Stack Viva Q&A

This guide contains the most critical viva questions categorized by topic to prepare you for your project presentation.

---

## Category 1: DBMS Concepts & Normalization

### Q1: What is the primary purpose of SRPT-LMS from a DBMS perspective?
**Answer:** It replaces error-prone manual spreadsheets with an ACID-compliant, 3rd Normal Form (3NF) relational database (`packaging_tracking`) that tracks the complete lifecycle state transitions of returnable physical assets, guarantees referential integrity via Foreign Keys, and provides instantaneous analytical reporting using Views and aggregate SQL queries.

### Q2: Explain the Normalization of your database up to 3NF.
**Answer:**
* **1NF (First Normal Form):** Every column contains atomic (indivisible) values. Primary keys (`user_id`, `asset_id`, etc.) uniquely identify records. No repeating groups.
* **2NF (Second Normal Form):** Satisfies 1NF and contains no partial functional dependencies (all non-key attributes are fully functionally dependent on the primary key).
* **3NF (Third Normal Form):** Satisfies 2NF and has **no transitive dependencies** ($A \rightarrow B$ and $B \rightarrow C$). For instance:
  * Customer details (phone, address, contact person) are stored in `customers`, and only `customer_id` is referenced in `issue_transactions`.
  * Packaging specifications (`capacity_kg`, `description`) are stored in `packaging_types`, not duplicated inside the `assets` table.

### Q3: What Foreign Key constraints and Cascade rules are implemented?
**Answer:** All child tables reference parents using `ON DELETE RESTRICT` (to prevent accidental deletion of warehouses or customers with active assets) and `ON UPDATE CASCADE` (to keep relational integrity intact if IDs update). For optional damage links in `repair_records`, `ON DELETE SET NULL` is used.

### Q4: Why did you create Database Views?
**Answer:** We created 3 SQL Views:
1. `view_inventory_summary`: Pre-aggregates asset fleet counts grouped by packaging type and warehouse hub.
2. `view_overdue_assets`: Dynamically computes `DATEDIFF(CURRENT_DATE, expected_return_date)` for currently `ISSUED` assets.
3. `view_customer_outstanding`: Computes net balance of assets held by each customer.
*Benefit:* Simplifies complex multi-table joins into a single virtual table, improves query maintainability, and provides security by exposing only computed metrics.

### Q5: What Indexes did you create and why?
**Answer:** We indexed high-frequency search and filter columns:
* `idx_assets_status` on `assets(status)`: Accelerates queries filtering available or damaged items.
* `idx_assets_code` on `assets(asset_code)`: Provides $O(\log N)$ unique asset lookup.
* `idx_issue_dates` on `issue_transactions(issue_date, expected_return_date)`: Speeds up overdue calculation queries.

---

## Category 2: Asset Lifecycle & Business Logic

### Q6: Walk me through the complete asset lifecycle state machine.
**Answer:**
1. **Registration:** Asset created $\rightarrow$ Status: `AVAILABLE`.
2. **Issue:** Warehouse staff dispatches asset to customer $\rightarrow$ Status becomes `ISSUED`.
3. **Return Inspection:** Customer returns asset:
   * If condition is `GOOD` $\rightarrow$ Status reverts to `AVAILABLE`.
   * If condition is `MINOR_DAMAGE` or `MAJOR_DAMAGE` $\rightarrow$ Status becomes `DAMAGED`.
   * If condition is `UNUSABLE` $\rightarrow$ Status becomes `RETIRED`.
4. **Repair:** Workshop technician opens repair ticket $\rightarrow$ Status becomes `UNDER_REPAIR`.
5. **Completion:** When repair ticket is marked `COMPLETED` $\rightarrow$ Status becomes `AVAILABLE` again for dispatch!

### Q7: How does the system prevent illegal actions (e.g. issuing a damaged asset)?
**Answer:** The `TransactionService.issueAsset()` method explicitly checks `asset.getStatus() == AssetStatus.AVAILABLE`. If the asset is `ISSUED`, `DAMAGED`, `UNDER_REPAIR`, or `LOST`, a custom `BadRequestException` is thrown, returning HTTP 400 Bad Request with a descriptive error message.

---

## Category 3: Spring Boot & Backend Architecture

### Q8: What is the 3-Tier Layered Backend Architecture?
**Answer:**
1. **Controller Layer (`@RestController`):** Handles incoming HTTP requests, input validation (`@Valid`), and serializes JSON responses.
2. **Service Layer (`@Service`):** Contains business logic, enforces lifecycle state transitions, and manages transactions (`@Transactional`).
3. **Repository Layer (`@Repository`):** Extends Spring Data JPA `JpaRepository` to interface with the MySQL database.

### Q9: How is Spring Data JPA different from plain JDBC?
**Answer:**
* In plain JDBC, developers must manually write SQL strings, open/close connections, map `ResultSet` rows to objects, and handle checked SQL exceptions.
* Spring Data JPA uses Hibernate ORM to automatically map Java `@Entity` classes to database tables and dynamically generates SQL queries at runtime based on method names (`findByStatus`, `existsByEmail`).

### Q10: How are passwords secured in the database?
**Answer:** Passwords are never stored in plain text. We use `BCryptPasswordEncoder` from `spring-security-crypto`, which applies a salt and one-way cryptographic hash algorithm before saving to the `users` table.

### Q11: How is Centralized Exception Handling implemented?
**Answer:** Using `@RestControllerAdvice` and `@ExceptionHandler` in `GlobalExceptionHandler.java`. It intercepts `ResourceNotFoundException` (404), `BadRequestException` (400), `ConflictException` (409), and `MethodArgumentNotValidException` (validation errors) to return standard, structured JSON payloads.

---

## Category 4: Frontend & REST API Integration

### Q12: How does the Frontend communicate with the Backend?
**Answer:** The frontend uses the standard JavaScript ES6 **Fetch API** (`apiFetch` in `api.js`) to make asynchronous HTTP requests (`GET`, `POST`, `PUT`, `DELETE`) with JSON payloads to Spring Boot REST endpoints on port `8080`.

### Q13: How is Role-Based Access Control (RBAC) handled?
**Answer:**
* On login, the user's role (`ADMIN`, `WAREHOUSE_STAFF`, `MANAGER`) is stored in `localStorage`.
* `auth.js` intercepts page navigation (`Auth.requireAuth(['ADMIN'])`) and dynamically renders sidebar menu items based on the active role.

---

## Category 5: Quick Viva Rapid Fire

| Question | Rapid Answer |
|---|---|
| **What is ACID in DBMS?** | Atomicity, Consistency, Isolation, Durability — guaranteed by MySQL InnoDB engine. |
| **What is an ORM?** | Object-Relational Mapping — translates Java class objects into relational database rows. |
| **What does `@Transactional` do?** | Commits all DB operations together or rolls back entirely if any error occurs (Atomicity). |
| **What is the difference between DDL and DML?** | DDL (`CREATE`, `ALTER`, `DROP`) defines schema; DML (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) manipulates data. |
| **What is a Surrogate Key vs Natural Key?** | `asset_id` (Auto-increment integer) is a Surrogate Key; `asset_code` (e.g. `MC-PUN-001`) is a Natural Key. |
