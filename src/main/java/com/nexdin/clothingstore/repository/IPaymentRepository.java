package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payments, String> {
}
