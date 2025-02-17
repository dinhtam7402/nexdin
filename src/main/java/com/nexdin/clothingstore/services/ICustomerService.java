package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Customers;
import com.nexdin.clothingstore.payload.request.CustomerRequest;

import java.util.List;

public interface ICustomerService {
    Customers getByID(String customerID);
    List<Customers> getAll();
    List<Customers> getByFullName(String fullName);
    List<Customers> getByEmail(String email);
    List<Customers> getByPhone(String phone);
    List<Customers> getByAddress(String address);
    Customers create(CustomerRequest request);
    Customers update(String customerID, CustomerRequest request);
    void delete(String customerID);
}
