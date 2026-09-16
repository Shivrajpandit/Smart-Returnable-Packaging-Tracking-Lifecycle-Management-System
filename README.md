# Smart Returnable Packaging Tracking and Lifecycle Management System (SRPT-LMS)

A complete beginner-friendly full-stack DBMS BTech capstone project built with **Java Spring Boot**, **Spring Data JPA**, **MySQL**, **HTML5**, **Bootstrap 5**, and **Vanilla JavaScript**.

---

## 📋 System Architecture

```
[ Frontend: HTML5 / CSS3 / Bootstrap 5 / Vanilla JS Fetch API ]
                          │
                   (REST API JSON)
                          ▼
[ Backend: Spring Boot 3.x / Controller / Service / Repository ]
                          │
                 (Spring Data JPA / ORM)
                          ▼
[ Database: MySQL 8.x (10 Normalized 3NF Tables & Views) ]
```

---

## 🗄️ Database Setup (MySQL)

1. Open **MySQL Workbench** or MySQL CLI.
2. Execute the DDL schema script:
   ```sql
   SOURCE database/schema.sql;
   ```
3. Execute the realistic sample dataset script:
   ```sql
   SOURCE database/sample_data.sql;
   ```

---

## ⚙️ Backend Setup (Spring Boot)

1. Open the project in **IntelliJ IDEA** (or VS Code).
2. Check `backend/src/main/resources/application.properties` and ensure your MySQL password matches:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   ```
3. Run the application:
   - Run `PackagingTrackingApplication.java`, OR
   - Run in terminal:
     ```bash
     cd backend
     mvn spring-boot:run
     ```
4. Server starts on: **`http://localhost:8080`**

---

## 🌐 Frontend Setup

1. Open `frontend/index.html` or `frontend/login.html` directly in your web browser or via VS Code **Live Server** (`http://127.0.0.1:5500/frontend/login.html`).

---

## 🔑 Default Demo Login Credentials

| Role | Email | Password | Privileges |
|---|---|---|---|
| **ADMIN** | `admin@packaging.com` | `admin123` | Full access: Users, Masters, Assets, Transactions, Reports |
| **WAREHOUSE_STAFF** | `staff@packaging.com` | `staff123` | Operational access: Issue, Return, Damage, Repair, Movement |
| **MANAGER** | `manager@packaging.com` | `manager123` | Analytical access: Dashboard, Overdue, Cost & Inventory Reports |

---

## 📁 Repository Structure

```
Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/
├── backend/                               # Spring Boot 3.2 Java Backend
│   ├── pom.xml                            # Maven Dependencies
│   └── src/main/java/com/packaging/
│       ├── PackagingTrackingApplication.java
│       ├── config/                        # CORS & Security Config
│       ├── controller/                    # 12 REST Controllers
│       ├── dto/                           # Request/Response DTOs
│       ├── entity/                        # 10 JPA Entities & 5 Enums
│       ├── exception/                     # Global Exception Handling
│       ├── repository/                    # 10 Spring Data JPA Repositories
│       └── service/                       # Business Logic & State Machines
├── frontend/                              # Responsive Web Interface
│   ├── css/styles.css                     # Custom Industrial CSS Theme
│   ├── js/api.js                          # Fetch API Client SDK
│   ├── js/auth.js                         # Dynamic Role-Based Nav
│   ├── index.html                         # Home / Landing Page
│   ├── login.html                         # Login Portal with Quick-Select
│   ├── dashboard.html                     # Real-Time KPI Dashboard
│   ├── assets.html                        # Asset Registry & Filters
│   ├── issue-asset.html                   # Outward Dispatch Workflow
│   ├── return-asset.html                  # Return & Inspection Workflow
│   ├── damage.html                        # Damage Incident Logs
│   ├── repairs.html                       # Workshop Repair Tickets
│   ├── movements.html                     # Facility Transfer Logs
│   ├── asset-history.html                 # 360° Timeline Audit View
│   ├── overdue.html                       # Overdue Returns Monitor
│   ├── reports.html                       # Manager Analytics & Summaries
│   ├── customers.html                     # Customer Management
│   ├── packaging-types.html               # Packaging Master Catalog
│   ├── warehouses.html                    # Facility Hubs Directory
│   └── users.html                         # User Administration (Admin)
├── database/
│   ├── schema.sql                         # 10 Tables, Constraints, Views, Indexes
│   ├── sample_data.sql                    # Realistic Dataset with 22 Assets
│   ├── queries.sql                        # Demonstration & Viva SQL Queries
│   └── reports.sql                        # Analytical Manager SQL Queries
└── documentation/
    ├── ER-diagram.md                      # Mermaid ER Diagram
    ├── DFD-level0-level1.md               # Context & Level 1 DFDs
    ├── usecase-and-class-diagrams.md      # UML Use Case & Class Diagrams
    ├── viva-questions.md                  # 30+ Comprehensive Viva Q&A
    ├── demonstration-guide.md             # Click-by-Click Presentation Guide
    ├── project-report.md                  # Formal Academic Report
    └── SRPT-LMS.postman_collection.json   # Postman Collection for Testing
```