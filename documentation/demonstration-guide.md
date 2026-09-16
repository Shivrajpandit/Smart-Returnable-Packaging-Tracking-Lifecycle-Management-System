# SRPT-LMS: Complete Step-by-Step Viva Demonstration Walkthrough

Follow this exact sequential demonstration flow during your college project presentation or viva examination.

---

## Pre-Demo Checklist

1. **MySQL Database Running:** Verify database `packaging_tracking` is created and populated with `schema.sql` and `sample_data.sql`.
2. **Spring Boot Backend Running:** Start via IntelliJ/Terminal (`mvn spring-boot:run`). Port: `8080`.
3. **Frontend Open:** Open `frontend/login.html` (or via Live Server `http://127.0.0.1:5500/frontend/login.html`).

---

## Live Demonstration Steps

### STEP 1: Login & Role-Based Navigation
- Open [`frontend/login.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/login.html).
- Click the **"Admin"** quick-fill button (`admin@packaging.com` / `admin123`) and click **Sign In**.
- **Point to Explain:** *Explain BCrypt encrypted authentication and dynamic role-based navigation rendering.*

---

### STEP 2: Dashboard Metrics & Overdue Overview
- You arrive at [`frontend/dashboard.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/dashboard.html).
- **Point to Explain:** *Show the 8 real-time KPI cards (Total fleet size, Available in stock, Issued, Damaged, Under Repair, Overdue Returns, and Total Repair Costs).*
- Scroll down to the **Overdue Return Warnings** table to show pre-existing overdue return alerts.

---

### STEP 3: Register a New Reusable Asset
- Go to [`frontend/assets.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/assets.html).
- Click **"Register Asset"** button.
- Enter:
  - **Asset Code:** `MC-PUN-555`
  - **Packaging Type:** `Heavy-Duty Collapsible Metal Container (1200 kg)`
  - **Warehouse Hub:** `Pune Central Logistics Hub`
  - **Purchase Date:** Current Date
- Click **Register Asset**.
- **Point to Explain:** *The asset is created in 3NF table `assets` with initial status `AVAILABLE`.*

---

### STEP 4: Issue the Asset to a Customer
- Go to [`frontend/issue-asset.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/issue-asset.html).
- Select your newly created asset `MC-PUN-555`.
- Select Customer: `Tata AutoComp Systems Ltd`.
- Set Expected Return Date (e.g. 10 days from today).
- Click **"Confirm Dispatch & Issue"**.
- **Point to Explain:** *State transition occurs: Asset status updates from `AVAILABLE` to `ISSUED`. An automatic movement record is generated logging transit from Warehouse to Customer.*

---

### STEP 5: Verify Illegal State Prevention (Edge Case)
- Try to issue `MC-PUN-555` again.
- **Point to Explain:** *Notice that `MC-PUN-555` is no longer in the "Available Assets" dropdown, preventing duplicate issues and enforcing business rule integrity.*

---

### STEP 6: Receive Inward Return with Damage Inspection
- Go to [`frontend/return-asset.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/return-asset.html).
- Select `MC-PUN-555` from the Issued Assets list.
- Select Customer: `Tata AutoComp Systems Ltd`.
- Set Condition Inspection: **`MINOR_DAMAGE`** (e.g., "Hinged side-gate bent").
- Click **"Confirm Inward Return"**.
- **Point to Explain:** *State transition occurs: Asset status becomes `DAMAGED` and inward transit back to warehouse is logged.*

---

### STEP 7: Create a Workshop Repair Ticket
- Go to [`frontend/repairs.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/repairs.html).
- Select Asset: `MC-PUN-555`.
- Set Repair Status: **`IN_PROGRESS`**.
- Set Initial Repair Cost: `₹ 650.00`.
- Remarks: "Straightening side-gate latch and reinforcement welding".
- Click **"Open Repair Ticket"**.
- **Point to Explain:** *Asset status transitions from `DAMAGED` to `UNDER_REPAIR`.*

---

### STEP 8: Complete Repair & Return to Available Stock
- In the **All Workshop Repair Tickets** table on `repairs.html`, find your ticket for `MC-PUN-555` and click **"Update"**.
- Change Repair Status to: **`COMPLETED`**.
- Set Final Cost: `₹ 650.00`.
- Click **"Save Ticket Changes"**.
- Go back to [`frontend/assets.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/assets.html).
- **Point to Explain:** *Notice `MC-PUN-555` is now back to `AVAILABLE` status and ready for dispatch again!*

---

### STEP 9: View the 360° Asset Lifecycle Timeline
- Go to [`frontend/asset-history.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/asset-history.html).
- Select `MC-PUN-555`.
- **Point to Explain:** *Show the complete chronological audit stream: Registration $\rightarrow$ Issue to Tata AutoComp $\rightarrow$ Inward Return with Minor Damage $\rightarrow$ Workshop Repair Completed $\rightarrow$ Movement Logs.*

---

### STEP 10: Managerial Analytics & Reports
- Switch user or open [`frontend/reports.html`](file:///c:/Users/pandi/Smart-Returnable-Packaging-Tracking-Lifecycle-Management-System/frontend/reports.html).
- Show the 4 analytical tabs:
  1. **Inventory Fleet Breakdown**
  2. **Customer Outstanding Return Matrix** (Shows net unreturned units per client)
  3. **Critical Overdue Returns** (Highlights overdue days)
  4. **Repair Cost Analysis** (`SUM(repair_cost)`)
