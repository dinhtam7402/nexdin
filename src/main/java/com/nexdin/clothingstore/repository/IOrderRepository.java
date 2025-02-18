package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Customers;
import com.nexdin.clothingstore.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Orders, String> {
    List<Orders> findByCustomers(Customers customer);
}
