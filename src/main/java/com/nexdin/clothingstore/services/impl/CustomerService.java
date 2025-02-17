package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Customers;
import com.nexdin.clothingstore.payload.request.CustomerRequest;
import com.nexdin.clothingstore.repository.ICustomerRepository;
import com.nexdin.clothingstore.services.ICustomerService;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public Customers getByID(String customerID) {
        Customers customer = customerRepository.findById(customerID).orElseThrow(
                () -> {
                    log.warn("[getByID] - Not found customer by ID: {}", customerID);
                    return new EntityNotFoundException("Not found customer by ID: " + customerID);
                });
        log.info("[getByID] - Found customer by ID: {}", customerID);
        return customer;
    }

    @Override
    public List<Customers> getAll() {
        List<Customers> customers = customerRepository.findAll();
        log.info("[getAll] - Retrieved {} customers successfully", customers.size());
        return customers;
    }

    @Override
    public List<Customers> getByFullName(String fullName) {
        List<Customers> customers = customerRepository.findByFullNameContaining(fullName);
        log.info("[getByFullName] - Retrieved {} customers successfully", customers.size());
        return customers;
    }

    @Override
    public List<Customers> getByEmail(String email) {
        List<Customers> customers = customerRepository.findByEmailContaining(email);
        log.info("[getByEmail] - Retrieved {} customers successfully", customers.size());
        return customers;
    }

    @Override
    public List<Customers> getByPhone(String phone) {
        List<Customers> customers = customerRepository.findByPhoneContaining(phone);
        log.info("[getByPhone] - Retrieved {} customers successfully", customers.size());
        return customers;
    }

    @Override
    public List<Customers> getByAddress(String address) {
        List<Customers> customers = customerRepository.findByAddressContaining(address);
        log.info("[getByAddress] - Retrieved {} customers successfully", customers.size());
        return customers;
    }

    @Override
    public Customers create(CustomerRequest request) {
        Customers customer = Customers.builder()
                .customerID(IDGenerate.generate())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        customerRepository.save(customer);
        log.info("[create] - Created customer '{}' successfully", customer.getCustomerID());
        return customer;
    }

    @Override
    public Customers update(String customerID, CustomerRequest request) {
        Customers oldCustomer = getByID(customerID);
        oldCustomer.setFullName(request.getFullName());
        oldCustomer.setEmail(request.getEmail());
        oldCustomer.setPhone(request.getPhone());
        oldCustomer.setAddress(request.getAddress());
        customerRepository.save(oldCustomer);
        log.info("[update] - Updated customer '{}' successfully", customerID);
        return oldCustomer;
    }

    @Override
    public void delete(String customerID) {
        Customers customer = getByID(customerID);
        customerRepository.delete(customer);
        log.info("[delete] - Deleted customer '{}' successfully", customerID);
    }
}
