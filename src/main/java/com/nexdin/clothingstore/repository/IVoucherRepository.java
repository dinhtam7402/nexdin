package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Vouchers;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IVoucherRepository extends JpaRepository<Vouchers, String>, JpaSpecificationExecutor<Vouchers> {
    Vouchers findByCode(String code);
    boolean existsByCode(String code);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vouchers v WHERE v.code = :code")
    Vouchers findAndLockByCode(@Param("code") String code);
}
