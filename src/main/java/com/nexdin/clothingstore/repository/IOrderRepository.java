package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IOrderRepository extends JpaRepository<Orders, String>, JpaSpecificationExecutor<Orders> {
}
