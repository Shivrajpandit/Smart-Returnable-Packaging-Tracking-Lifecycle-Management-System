package com.packaging.service;

import com.packaging.dto.CustomerDTO;
import com.packaging.entity.Customer;
import com.packaging.exception.ConflictException;
import com.packaging.exception.ResourceNotFoundException;
import com.packaging.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> searchCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return customerRepository.findAll();
        }
        return customerRepository.searchCustomers(query.trim());
    }

    public Customer getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }

    @Transactional
    public Customer createCustomer(CustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new ConflictException("Customer with email '" + dto.getEmail() + "' already exists");
        }

        Customer customer = new Customer();
        customer.setCompanyName(dto.getCompanyName().trim());
        customer.setContactPerson(dto.getContactPerson().trim());
        customer.setPhone(dto.getPhone().trim());
        customer.setEmail(dto.getEmail().trim().toLowerCase());
        customer.setAddress(dto.getAddress().trim());

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Integer customerId, CustomerDTO dto) {
        Customer customer = getCustomerById(customerId);

        customer.setCompanyName(dto.getCompanyName().trim());
        customer.setContactPerson(dto.getContactPerson().trim());
        customer.setPhone(dto.getPhone().trim());
        customer.setEmail(dto.getEmail().trim().toLowerCase());
        customer.setAddress(dto.getAddress().trim());

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }
}
