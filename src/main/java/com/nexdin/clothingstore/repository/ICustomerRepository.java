package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICustomerRepository extends JpaRepository<Customers, String> {
    List<Customers> findByFullNameContaining(String fullName);
    List<Customers> findByEmailContaining(String email);
    List<Customers> findByPhoneContaining(String phone);
    List<Customers> findByAddressContaining(String address);
}
