/**
 * SRPT-LMS: Centralized JavaScript REST API Client
 */

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Core HTTP Request Wrapper using Fetch API
 */
async function apiFetch(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  const config = {
    ...options,
    headers
  };

  try {
    const response = await fetch(url, config);
    
    // Handle 204 No Content
    if (response.status === 204) {
      return null;
    }

    const data = await response.json().catch(() => null);

    if (!response.ok) {
      let errorMsg = data?.message || `Request failed with status ${response.status}`;
      if (data?.validationErrors) {
        const details = Object.entries(data.validationErrors).map(([k, v]) => `${k}: ${v}`).join(', ');
        errorMsg += ` (${details})`;
      }
      throw new Error(errorMsg);
    }

    return data;
  } catch (error) {
    console.error(`API Error on [${options.method || 'GET'} ${endpoint}]:`, error);
    showNotification(error.message, 'danger');
    throw error;
  }
}

/**
 * Toast Notification Helper
 */
function showNotification(message, type = 'success') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `alert alert-${type} alert-dismissible fade show shadow-sm`;
  toast.role = 'alert';
  toast.style.minWidth = '300px';
  toast.innerHTML = `
    <strong>${type === 'danger' ? 'Error!' : 'Success!'}</strong> ${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  `;

  container.appendChild(toast);
  setTimeout(() => {
    toast.classList.remove('show');
    setTimeout(() => toast.remove(), 200);
  }, 4500);
}

/**
 * SRPT-LMS API Service SDK
 */
const API = {
  // Authentication
  auth: {
    login: (credentials) => apiFetch('/auth/login', { method: 'POST', body: JSON.stringify(credentials) }),
    register: (userData) => apiFetch('/auth/register', { method: 'POST', body: JSON.stringify(userData) })
  },

  // Reports & Dashboard
  reports: {
    getDashboardMetrics: () => apiFetch('/reports/dashboard'),
    getOverdue: () => apiFetch('/reports/overdue'),
    getCustomerSummary: () => apiFetch('/reports/customer-summary')
  },

  // Customers
  customers: {
    getAll: (search = '') => apiFetch(`/customers${search ? `?search=${encodeURIComponent(search)}` : ''}`),
    getById: (id) => apiFetch(`/customers/${id}`),
    create: (data) => apiFetch('/customers', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/customers/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/customers/${id}`, { method: 'DELETE' })
  },

  // Packaging Types
  packagingTypes: {
    getAll: () => apiFetch('/packaging-types'),
    getById: (id) => apiFetch(`/packaging-types/${id}`),
    create: (data) => apiFetch('/packaging-types', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/packaging-types/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/packaging-types/${id}`, { method: 'DELETE' })
  },

  // Warehouses
  warehouses: {
    getAll: () => apiFetch('/warehouses'),
    getById: (id) => apiFetch(`/warehouses/${id}`),
    create: (data) => apiFetch('/warehouses', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/warehouses/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/warehouses/${id}`, { method: 'DELETE' })
  },

  // Assets
  assets: {
    getAll: (filters = {}) => {
      const params = new URLSearchParams();
      if (filters.status) params.append('status', filters.status);
      if (filters.typeId) params.append('typeId', filters.typeId);
      if (filters.warehouseId) params.append('warehouseId', filters.warehouseId);
      if (filters.search) params.append('search', filters.search);
      const queryString = params.toString();
      return apiFetch(`/assets${queryString ? `?${queryString}` : ''}`);
    },
    getById: (id) => apiFetch(`/assets/${id}`),
    getByCode: (code) => apiFetch(`/assets/code/${encodeURIComponent(code)}`),
    getHistory: (id) => apiFetch(`/assets/${id}/history`),
    create: (data) => apiFetch('/assets', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/assets/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/assets/${id}`, { method: 'DELETE' })
  },

  // Transactions & Operations
  issues: {
    getAll: () => apiFetch('/issues'),
    getById: (id) => apiFetch(`/issues/${id}`),
    create: (data) => apiFetch('/issues', { method: 'POST', body: JSON.stringify(data) })
  },

  returns: {
    getAll: () => apiFetch('/returns'),
    getById: (id) => apiFetch(`/returns/${id}`),
    create: (data) => apiFetch('/returns', { method: 'POST', body: JSON.stringify(data) })
  },

  damages: {
    getAll: () => apiFetch('/damages'),
    getById: (id) => apiFetch(`/damages/${id}`),
    create: (data) => apiFetch('/damages', { method: 'POST', body: JSON.stringify(data) })
  },

  repairs: {
    getAll: () => apiFetch('/repairs'),
    getById: (id) => apiFetch(`/repairs/${id}`),
    create: (data) => apiFetch('/repairs', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/repairs/${id}`, { method: 'PUT', body: JSON.stringify(data) })
  },

  movements: {
    getAll: () => apiFetch('/movements'),
    create: (data) => apiFetch('/movements', { method: 'POST', body: JSON.stringify(data) })
  },

  users: {
    getAll: () => apiFetch('/users'),
    getById: (id) => apiFetch(`/users/${id}`),
    create: (data) => apiFetch('/users', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/users/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/users/${id}`, { method: 'DELETE' })
  }
};
