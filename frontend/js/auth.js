/**
 * SRPT-LMS: Authentication & Role-Based Navigation Manager
 */

const AUTH_KEY = 'srpt_lms_user';

const Auth = {
  // Save logged-in user in localStorage
  setUser(user) {
    localStorage.setItem(AUTH_KEY, JSON.stringify(user));
  },

  // Retrieve current user
  getUser() {
    const data = localStorage.getItem(AUTH_KEY);
    return data ? JSON.parse(data) : null;
  },

  // Clear session
  logout() {
    localStorage.removeItem(AUTH_KEY);
    window.location.href = 'login.html';
  },

  // Guard page: redirect if not logged in
  requireAuth(allowedRoles = []) {
    const user = this.getUser();
    if (!user) {
      window.location.href = 'login.html';
      return null;
    }

    if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
      alert(`Access Restricted: This module requires ${allowedRoles.join(' or ')} permissions.`);
      window.location.href = 'dashboard.html';
      return null;
    }

    return user;
  },

  // Render unified sidebar and navbar components dynamically
  initNavigation(activePageId) {
    const user = this.getUser();
    const role = user ? user.role : 'GUEST';
    const name = user ? user.name : 'Guest User';

    // 1. Render Topbar User Badge
    const userBadgeEl = document.getElementById('user-profile-badge');
    if (userBadgeEl && user) {
      userBadgeEl.innerHTML = `
        <div class="d-flex align-items-center gap-2">
          <div class="text-end d-none d-md-block">
            <div class="fw-bold text-dark" style="font-size: 0.88rem;">${name}</div>
            <span class="role-pill role-${role}">${role.replace('_', ' ')}</span>
          </div>
          <button class="btn btn-outline-danger btn-sm ms-2" onclick="Auth.logout()" title="Sign Out">
            <i class="bi bi-box-arrow-right"></i>
          </button>
        </div>
      `;
    }

    // 2. Render Sidebar Tailored to Role
    const sidebarEl = document.getElementById('sidebar-container');
    if (sidebarEl) {
      let navItemsHtml = '';

      if (role === 'ADMIN') {
        navItemsHtml = `
          <div class="sidebar-category">Overview</div>
          <li>
            <a href="dashboard.html" class="nav-link-custom ${activePageId === 'dashboard' ? 'active' : ''}">
              <i class="bi bi-grid-1x2-fill"></i> Admin Dashboard
            </a>
          </li>

          <div class="sidebar-category">Fleet Operations</div>
          <li>
            <a href="assets.html" class="nav-link-custom ${activePageId === 'assets' ? 'active' : ''}">
              <i class="bi bi-box-seam"></i> Asset Registry
            </a>
          </li>
          <li>
            <a href="issue-asset.html" class="nav-link-custom ${activePageId === 'issue' ? 'active' : ''}">
              <i class="bi bi-box-arrow-up-right"></i> Issue Asset
            </a>
          </li>
          <li>
            <a href="return-asset.html" class="nav-link-custom ${activePageId === 'return' ? 'active' : ''}">
              <i class="bi bi-box-arrow-in-down-left"></i> Receive Return
            </a>
          </li>
          <li>
            <a href="damage.html" class="nav-link-custom ${activePageId === 'damage' ? 'active' : ''}">
              <i class="bi bi-exclamation-triangle"></i> Damage Logs
            </a>
          </li>
          <li>
            <a href="repairs.html" class="nav-link-custom ${activePageId === 'repairs' ? 'active' : ''}">
              <i class="bi bi-tools"></i> Repair Tickets
            </a>
          </li>
          <li>
            <a href="movements.html" class="nav-link-custom ${activePageId === 'movements' ? 'active' : ''}">
              <i class="bi bi-truck"></i> Asset Movements
            </a>
          </li>

          <div class="sidebar-category">Analytics & Reports</div>
          <li>
            <a href="overdue.html" class="nav-link-custom ${activePageId === 'overdue' ? 'active' : ''}">
              <i class="bi bi-alarm-fill text-danger"></i> Overdue Monitor
            </a>
          </li>
          <li>
            <a href="reports.html" class="nav-link-custom ${activePageId === 'reports' ? 'active' : ''}">
              <i class="bi bi-bar-chart-line-fill"></i> Manager Reports
            </a>
          </li>

          <div class="sidebar-category">Master Data</div>
          <li>
            <a href="customers.html" class="nav-link-custom ${activePageId === 'customers' ? 'active' : ''}">
              <i class="bi bi-buildings"></i> Customers
            </a>
          </li>
          <li>
            <a href="packaging-types.html" class="nav-link-custom ${activePageId === 'packaging-types' ? 'active' : ''}">
              <i class="bi bi-tags"></i> Packaging Types
            </a>
          </li>
          <li>
            <a href="warehouses.html" class="nav-link-custom ${activePageId === 'warehouses' ? 'active' : ''}">
              <i class="bi bi-geo-alt"></i> Warehouses
            </a>
          </li>

          <div class="sidebar-category">System</div>
          <li>
            <a href="users.html" class="nav-link-custom ${activePageId === 'users' ? 'active' : ''}">
              <i class="bi bi-people"></i> User Directory
            </a>
          </li>
        `;
      } else if (role === 'WAREHOUSE_STAFF') {
        navItemsHtml = `
          <div class="sidebar-category">Overview</div>
          <li>
            <a href="dashboard.html" class="nav-link-custom ${activePageId === 'dashboard' ? 'active' : ''}">
              <i class="bi bi-speedometer2"></i> Staff Dashboard
            </a>
          </li>

          <div class="sidebar-category">Floor Operations</div>
          <li>
            <a href="assets.html" class="nav-link-custom ${activePageId === 'assets' ? 'active' : ''}">
              <i class="bi bi-box-seam"></i> Asset Registry
            </a>
          </li>
          <li>
            <a href="issue-asset.html" class="nav-link-custom ${activePageId === 'issue' ? 'active' : ''}">
              <i class="bi bi-box-arrow-up-right text-primary"></i> Issue Asset (Dispatch)
            </a>
          </li>
          <li>
            <a href="return-asset.html" class="nav-link-custom ${activePageId === 'return' ? 'active' : ''}">
              <i class="bi bi-box-arrow-in-down-left text-success"></i> Receive Return
            </a>
          </li>
          <li>
            <a href="damage.html" class="nav-link-custom ${activePageId === 'damage' ? 'active' : ''}">
              <i class="bi bi-exclamation-triangle text-warning"></i> Log Damage
            </a>
          </li>
          <li>
            <a href="repairs.html" class="nav-link-custom ${activePageId === 'repairs' ? 'active' : ''}">
              <i class="bi bi-tools text-secondary"></i> Repair Workshop
            </a>
          </li>
          <li>
            <a href="movements.html" class="nav-link-custom ${activePageId === 'movements' ? 'active' : ''}">
              <i class="bi bi-truck"></i> Asset Movements
            </a>
          </li>

          <div class="sidebar-category">Alerts</div>
          <li>
            <a href="overdue.html" class="nav-link-custom ${activePageId === 'overdue' ? 'active' : ''}">
              <i class="bi bi-alarm-fill text-danger"></i> Overdue Monitor
            </a>
          </li>
        `;
      } else if (role === 'MANAGER') {
        navItemsHtml = `
          <div class="sidebar-category">Overview</div>
          <li>
            <a href="dashboard.html" class="nav-link-custom ${activePageId === 'dashboard' ? 'active' : ''}">
              <i class="bi bi-bar-chart-line"></i> Manager Dashboard
            </a>
          </li>

          <div class="sidebar-category">Intelligence & Audits</div>
          <li>
            <a href="reports.html" class="nav-link-custom ${activePageId === 'reports' ? 'active' : ''}">
              <i class="bi bi-file-earmark-bar-graph-fill text-primary"></i> Financial & Fleet Reports
            </a>
          </li>
          <li>
            <a href="overdue.html" class="nav-link-custom ${activePageId === 'overdue' ? 'active' : ''}">
              <i class="bi bi-alarm-fill text-danger"></i> Overdue Monitor
            </a>
          </li>

          <div class="sidebar-category">Fleet & Directory</div>
          <li>
            <a href="assets.html" class="nav-link-custom ${activePageId === 'assets' ? 'active' : ''}">
              <i class="bi bi-box-seam"></i> Asset Registry
            </a>
          </li>
          <li>
            <a href="customers.html" class="nav-link-custom ${activePageId === 'customers' ? 'active' : ''}">
              <i class="bi bi-buildings"></i> Customer Accounts
            </a>
          </li>
        `;
      } else {
        navItemsHtml = `
          <div class="sidebar-category">Overview</div>
          <li>
            <a href="dashboard.html" class="nav-link-custom ${activePageId === 'dashboard' ? 'active' : ''}">
              <i class="bi bi-grid-1x2-fill"></i> Dashboard
            </a>
          </li>
        `;
      }

      sidebarEl.innerHTML = `
        <div class="sidebar">
          <div class="sidebar-header">
            <i class="bi bi-boxes text-primary fs-3"></i>
            <div>
              <a href="dashboard.html" class="sidebar-brand d-block">SRPT-LMS</a>
              <small class="text-secondary" style="font-size: 0.7rem;">Returnable Asset Core</small>
            </div>
          </div>

          <ul class="sidebar-nav">
            ${navItemsHtml}
          </ul>
        </div>
      `;
    }
  }
};
