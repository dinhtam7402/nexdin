package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IVoucherRepository extends JpaRepository<Vouchers, String>, JpaSpecificationExecutor<Vouchers> {
    Vouchers findByCode(String code);
    boolean existsByCode(String code);
}
